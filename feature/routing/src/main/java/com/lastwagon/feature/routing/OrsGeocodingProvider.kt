package com.lastwagon.feature.routing

import com.lastwagon.core.model.GeoPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLEncoder

// ORS free-tier geocoding allows on the order of 100 requests per minute against a shared
// daily quota, so autocomplete waits for a typing pause and skips queries too short to match.
internal const val AUTOCOMPLETE_DEBOUNCE_MILLIS = 450L
internal const val AUTOCOMPLETE_MIN_CHARS = 3
internal const val AUTOCOMPLETE_MAX_SUGGESTIONS = 6

private const val DEFAULT_ORS_GEOCODE_BASE_URL = "https://api.openrouteservice.org/geocode"

private const val MISSING_KEY_MESSAGE =
    "OpenRouteService is not configured. Set ORS_API_KEY locally and rebuild."

data class GeocodeSuggestion(val label: String, val point: GeoPoint)

sealed interface GeocodeLookupResult {
    data class Success(val suggestions: List<GeocodeSuggestion>) : GeocodeLookupResult
    data class Failure(
        val message: String,
        val httpStatus: Int? = null,
        val retryable: Boolean = false,
    ) : GeocodeLookupResult
}

interface GeocodingProvider {
    val id: String

    /** Search-as-you-type lookup. [focus] biases result ranking toward a nearby point. */
    suspend fun autocomplete(text: String, focus: GeoPoint? = null): GeocodeLookupResult

    /** Resolves coordinates to a human-readable label so the driver can confirm them. */
    suspend fun reverse(point: GeoPoint): GeocodeLookupResult
}

internal fun interface GeocodingHttpTransport {
    suspend fun get(url: String, authorization: String): RoutingHttpResponse
}

internal class UrlConnectionGeocodingTransport : GeocodingHttpTransport {
    override suspend fun get(url: String, authorization: String) = withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            connection.setRequestProperty("Authorization", authorization)
            connection.setRequestProperty("Accept", "application/geo+json, application/json")
            val status = connection.responseCode
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            RoutingHttpResponse(status, stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty())
        } finally {
            connection.disconnect()
        }
    }
}

class OrsGeocodingProvider internal constructor(
    private val apiKey: String,
    private val transport: GeocodingHttpTransport,
    private val baseUrl: String,
) : GeocodingProvider {
    constructor(apiKey: String) : this(
        apiKey.trim(), UrlConnectionGeocodingTransport(), DEFAULT_ORS_GEOCODE_BASE_URL,
    )

    override val id = "openrouteservice-geocode"

    override suspend fun autocomplete(text: String, focus: GeoPoint?): GeocodeLookupResult {
        if (apiKey.isBlank()) return GeocodeLookupResult.Failure(MISSING_KEY_MESSAGE)
        val query = text.trim()
        if (query.length < AUTOCOMPLETE_MIN_CHARS) return GeocodeLookupResult.Success(emptyList())
        val url = buildString {
            append(baseUrl).append("/autocomplete")
            append("?text=").append(URLEncoder.encode(query, "UTF-8"))
            // The first milestone serves United States commercial drivers only.
            append("&boundary.country=US")
            append("&size=").append(AUTOCOMPLETE_MAX_SUGGESTIONS)
            if (focus != null && focus.isValid) {
                append("&focus.point.lon=").append(focus.longitude)
                append("&focus.point.lat=").append(focus.latitude)
            }
        }
        return fetch(url)
    }

    override suspend fun reverse(point: GeoPoint): GeocodeLookupResult {
        if (apiKey.isBlank()) return GeocodeLookupResult.Failure(MISSING_KEY_MESSAGE)
        if (!point.isValid) return GeocodeLookupResult.Failure("The location to look up is invalid.")
        return fetch("$baseUrl/reverse?point.lon=${point.longitude}&point.lat=${point.latitude}&size=1")
    }

    private suspend fun fetch(url: String): GeocodeLookupResult {
        val response = try {
            transport.get(url, apiKey)
        } catch (_: SocketTimeoutException) {
            return GeocodeLookupResult.Failure("The address service timed out.", retryable = true)
        } catch (_: Exception) {
            return GeocodeLookupResult.Failure("The address service could not be reached.", retryable = true)
        }
        if (response.status !in 200..299) return providerFailure(response)
        return parse(response.body)
    }

    private fun parse(body: String): GeocodeLookupResult = try {
        val features = Json.parseToJsonElement(body).jsonObject["features"]?.jsonArray.orEmpty()
        val suggestions = features.mapNotNull { feature ->
            val obj = feature.jsonObject
            val coordinates = obj["geometry"]?.jsonObject?.get("coordinates")?.jsonArray
                ?: return@mapNotNull null
            val point = GeoPoint(
                coordinates[1].jsonPrimitive.double,
                coordinates[0].jsonPrimitive.double,
            )
            val label = obj["properties"]?.jsonObject?.get("label")?.jsonPrimitive?.contentOrNull
            if (label.isNullOrBlank() || !point.isValid) null else GeocodeSuggestion(label, point)
        }
        GeocodeLookupResult.Success(suggestions)
    } catch (_: Exception) {
        GeocodeLookupResult.Failure("The address service response could not be read.")
    }

    private fun providerFailure(response: RoutingHttpResponse): GeocodeLookupResult.Failure {
        val message = runCatching {
            val error = Json.parseToJsonElement(response.body).jsonObject["error"]
            error?.jsonObject?.get("message")?.jsonPrimitive?.contentOrNull
                ?: error?.jsonPrimitive?.contentOrNull
        }.getOrNull() ?: when (response.status) {
            429 -> "The address service request limit was reached. Wait a moment and try again."
            else -> "The address service rejected the request."
        }
        return GeocodeLookupResult.Failure(
            message, response.status, response.status == 429 || response.status >= 500,
        )
    }
}
