package com.lastwagon.feature.routing

import com.lastwagon.core.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.security.MessageDigest
import java.util.UUID

private const val DEFAULT_ORS_ENDPOINT = OrsApi.DIRECTIONS_HGV_GEOJSON_URL

internal data class RoutingHttpResponse(val status: Int, val body: String)

internal fun interface RoutingHttpTransport {
    suspend fun post(url: String, authorization: String, body: String): RoutingHttpResponse
}

internal class UrlConnectionRoutingTransport : RoutingHttpTransport {
    override suspend fun post(url: String, authorization: String, body: String) =
        withContext(Dispatchers.IO) {
            val connection = URL(url).openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "POST"
                connection.connectTimeout = 15_000
                connection.readTimeout = 30_000
                connection.doOutput = true
                connection.setRequestProperty("Authorization", authorization)
                connection.setRequestProperty("Accept", "application/geo+json, application/json")
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                connection.outputStream.bufferedWriter(Charsets.UTF_8).use { it.write(body) }
                val status = connection.responseCode
                val stream = if (status in 200..299) connection.inputStream else connection.errorStream
                RoutingHttpResponse(status, stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty())
            } finally {
                connection.disconnect()
            }
        }
}

class OrsRoutingProvider internal constructor(
    private val apiKey: String,
    private val transport: RoutingHttpTransport,
    private val endpoint: String,
    private val now: () -> Long,
    private val requestId: () -> String,
) : RoutingProvider {
    constructor(apiKey: String) : this(
        apiKey.trim(), UrlConnectionRoutingTransport(), DEFAULT_ORS_ENDPOINT,
        System::currentTimeMillis, { UUID.randomUUID().toString() },
    )

    override val id = "openrouteservice"

    override suspend fun calculate(request: RouteRequest): RouteCalculationResult {
        if (apiKey.isBlank()) return failure(
            RouteFailureKind.MISSING_CREDENTIAL,
            "OpenRouteService is not configured. Paste an API key under Settings, " +
                "or set ORS_API_KEY at build time.",
        )
        if (!request.origin.isValid || !request.destination.isValid ||
            request.origin == request.destination ||
            VehicleProfileValidator.validate(request.vehicleProfile).isNotEmpty()
        ) return failure(RouteFailureKind.INVALID_REQUEST, "Route endpoints or vehicle profile are invalid.")

        val payload = buildPayload(request)
        val requestedAt = now()
        val id = requestId()
        val response = try {
            transport.post(endpoint, apiKey, payload)
        } catch (_: SocketTimeoutException) {
            return failure(RouteFailureKind.TIMEOUT, "The routing provider timed out.", retryable = true)
        } catch (_: Exception) {
            return failure(RouteFailureKind.NETWORK, "The routing provider could not be reached.", retryable = true)
        }
        val receivedAt = now()

        if (response.status !in 200..299) return providerFailure(response)
        return parseSuccess(request, payload, id, requestedAt, receivedAt, response.body)
    }

    internal fun buildPayload(request: RouteRequest): String = buildJsonObject {
        putJsonArray("coordinates") {
            addJsonArray { add(request.origin.longitude); add(request.origin.latitude) }
            addJsonArray { add(request.destination.longitude); add(request.destination.latitude) }
        }
        put("instructions", true)
        put("preference", "recommended")
        put("units", "m")
        putJsonArray("extra_info") { add("roadaccessrestrictions") }
        putJsonObject("options") {
            put("vehicle_type", request.vehicleProfile.orsVehicleType())
            val avoidFeatures = buildList {
                if (request.vehicleProfile.avoidTolls) add("tollways")
                if (request.vehicleProfile.avoidFerries) add("ferries")
            }
            if (avoidFeatures.isNotEmpty()) putJsonArray("avoid_features") {
                avoidFeatures.forEach(::add)
            }
            putJsonObject("profile_params") {
                putJsonObject("restrictions") {
                    put("length", request.vehicleProfile.lengthMeters)
                    put("width", request.vehicleProfile.widthMeters)
                    put("height", request.vehicleProfile.heightMeters)
                    put("axleload", request.vehicleProfile.axleLoadTonnes)
                    put("weight", request.vehicleProfile.grossWeightTonnes)
                    put("hazmat", request.vehicleProfile.hazmat)
                }
            }
        }
    }.toString()

    private fun parseSuccess(
        request: RouteRequest, payload: String, id: String, requestedAt: Long,
        receivedAt: Long, body: String,
    ): RouteCalculationResult {
        return try {
        val root = Json.parseToJsonElement(body).jsonObject
        val feature = root["features"]?.jsonArray?.firstOrNull()?.jsonObject
            ?: return failure(RouteFailureKind.NO_ROUTE, "The provider returned no route.")
        val coordinates = feature["geometry"]?.jsonObject?.get("coordinates")?.jsonArray
            ?.map { coordinate ->
                val pair = coordinate.jsonArray
                GeoPoint(pair[1].jsonPrimitive.double, pair[0].jsonPrimitive.double)
            }.orEmpty()
        if (coordinates.size < 2 || coordinates.any { !it.isValid })
            return failure(RouteFailureKind.MALFORMED_RESPONSE, "The provider returned invalid route geometry.")

        val properties = feature["properties"]!!.jsonObject
        val summary = properties["summary"]!!.jsonObject
        val steps = properties["segments"]?.jsonArray.orEmpty().flatMap { segment ->
            segment.jsonObject["steps"]?.jsonArray.orEmpty().map { value ->
                val step = value.jsonObject
                RouteStep(
                    step["instruction"]?.jsonPrimitive?.content.orEmpty(),
                    step["distance"]?.jsonPrimitive?.doubleOrNull ?: 0.0,
                    step["duration"]?.jsonPrimitive?.doubleOrNull ?: 0.0,
                )
            }
        }
        val restrictionCount = properties["extras"]?.jsonObject
            ?.get("roadaccessrestrictions")?.jsonObject?.get("values")?.jsonArray?.size ?: 0
        val metadata = root["metadata"]?.jsonObject
        val engine = metadata?.get("engine")?.jsonObject
        val warnings = buildList {
            add("Calculated from available map and restriction data; driver verification is required.")
            if (request.vehicleProfile.avoidUnpavedRoads) add(
                "OpenRouteService does not support an unpaved-road avoidance flag; that preference was not applied.")
            if (restrictionCount == 0) add(
                "No road-access restriction segments were reported; this does not prove restrictions are absent.")
        }
        RouteCalculationResult.Success(CalculatedRoute(
            request, coordinates,
            summary["distance"]!!.jsonPrimitive.double,
            summary["duration"]!!.jsonPrimitive.double,
            steps, warnings, restrictionCount,
            RouteProvenance(
                id, this.id, endpoint, "driving-hgv", requestedAt, receivedAt, payload,
                body.sha256(), metadata?.get("attribution")?.jsonPrimitive?.contentOrNull,
                metadata?.get("service")?.jsonPrimitive?.contentOrNull,
                metadata?.get("timestamp")?.jsonPrimitive?.longOrNull,
                engine?.get("version")?.jsonPrimitive?.contentOrNull,
                engine?.get("build_date")?.jsonPrimitive?.contentOrNull,
                engine?.get("graph_date")?.jsonPrimitive?.contentOrNull,
            ),
        ))
        } catch (_: Exception) {
            failure(RouteFailureKind.MALFORMED_RESPONSE, "The provider response could not be validated.")
        }
    }

    private fun providerFailure(response: RoutingHttpResponse): RouteCalculationResult.Failure {
        // ORS returns errors both as {"error":{"message":...}} and as {"error":"..."}.
        val message = runCatching {
            when (val error = Json.parseToJsonElement(response.body).jsonObject["error"]) {
                is JsonObject -> error["message"]?.jsonPrimitive?.contentOrNull
                is JsonPrimitive -> error.contentOrNull
                else -> null
            }
        }.getOrNull() ?: "The routing provider rejected the request."
        val kind = when (response.status) {
            429 -> RouteFailureKind.RATE_LIMITED
            else -> RouteFailureKind.PROVIDER_REJECTED
        }
        return failure(kind, message, response.status, response.status == 429 || response.status >= 500)
    }

    private fun failure(
        kind: RouteFailureKind, message: String, status: Int? = null, retryable: Boolean = false,
    ) = RouteCalculationResult.Failure(RouteFailure(kind, message, status, retryable))
}

private fun VehicleProfile.orsVehicleType() = when (vehicleType) {
    CommercialVehicleType.BUS -> "bus"
    CommercialVehicleType.DELIVERY -> "delivery"
    else -> "hgv"
}

private fun String.sha256(): String = MessageDigest.getInstance("SHA-256")
    .digest(toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) }
