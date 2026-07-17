package com.lastwagon.feature.routing

import com.lastwagon.core.model.GeoPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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

private const val DEFAULT_ORS_GEOCODE_BASE_URL = OrsApi.GEOCODE_BASE_URL

private const val MISSING_KEY_MESSAGE =
    "OpenRouteService is not configured. Paste an API key under Settings, " +
        "or set ORS_API_KEY at build time."

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
    override suspend fun get(url: String, authorization: String): RoutingHttpResponse = coroutineScope {
        val connection = withContext(Dispatchers.IO) { URL(url).openConnection() as HttpURLConnection }
        // A cancelled lookup (the user kept typing) must abort the blocking exchange right
        // away instead of holding the socket until its timeout; disconnect() from another
        // coroutine is the only interruption HttpURLConnection supports.
        var exchangeDone = false
        val cancellationWatcher = launch {
            try {
                awaitCancellation()
            } finally {
                if (!exchangeDone) runCatching { connection.disconnect() }
            }
        }
        try {
            withContext(Dispatchers.IO) {
                connection.requestMethod = "GET"
                connection.connectTimeout = 10_000
                connection.readTimeout = 10_000
                connection.setRequestProperty("Authorization", authorization)
                connection.setRequestProperty("Accept", "application/geo+json, application/json")
                val status = connection.responseCode
                val stream = if (status in 200..299) connection.inputStream else connection.errorStream
                RoutingHttpResponse(status, stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty())
            }
        } finally {
            exchangeDone = true
            cancellationWatcher.cancel()
            runCatching { connection.disconnect() }
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
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (_: Exception) {
            return GeocodeLookupResult.Failure("The address service could not be reached.", retryable = true)
        }
        if (response.status !in 200..299) return providerFailure(response)
        return parse(response.body)
    }

    // Individual malformed features are skipped so one bad entry cannot discard the
    // remaining valid suggestions; only an unparseable body fails the whole lookup.
    private fun parse(body: String): GeocodeLookupResult = try {
        val features = (Json.parseToJsonElement(body) as? JsonObject)
            ?.get("features") as? JsonArray ?: JsonArray(emptyList())
        val suggestions = features.mapNotNull { feature ->
            val obj = feature as? JsonObject ?: return@mapNotNull null
            val geometry = obj["geometry"] as? JsonObject ?: return@mapNotNull null
            val coordinates = geometry["coordinates"] as? JsonArray ?: return@mapNotNull null
            val latitude = (coordinates.getOrNull(1) as? JsonPrimitive)?.doubleOrNull
                ?: return@mapNotNull null
            val longitude = (coordinates.getOrNull(0) as? JsonPrimitive)?.doubleOrNull
                ?: return@mapNotNull null
            val point = GeoPoint(latitude, longitude)
            val properties = obj["properties"] as? JsonObject
            val label = (properties?.get("label") as? JsonPrimitive)?.contentOrNull
            if (label.isNullOrBlank() || !point.isValid) null else GeocodeSuggestion(label, point)
        }
        GeocodeLookupResult.Success(suggestions)
    } catch (_: Exception) {
        GeocodeLookupResult.Failure("The address service response could not be read.")
    }

    private fun providerFailure(response: RoutingHttpResponse): GeocodeLookupResult.Failure {
        // ORS returns errors both as {"error":{"message":...}} and as {"error":"..."}.
        val message = runCatching {
            when (val error = Json.parseToJsonElement(response.body).jsonObject["error"]) {
                is JsonObject -> error["message"]?.jsonPrimitive?.contentOrNull
                is JsonPrimitive -> error.contentOrNull
                else -> null
            }
        }.getOrNull() ?: when (response.status) {
            429 -> "The address service request limit was reached. Wait a moment and try again."
            else -> "The address service rejected the request."
        }
        return GeocodeLookupResult.Failure(
            message, response.status, response.status == 429 || response.status >= 500,
        )
    }
}
