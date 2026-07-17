package com.lastwagon.feature.routing

import com.lastwagon.core.model.GeoPoint
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class OrsGeocodingProviderTest {
    private class RecordingTransport(private val response: RoutingHttpResponse) : GeocodingHttpTransport {
        val urls = mutableListOf<String>()
        val authorizations = mutableListOf<String>()
        override suspend fun get(url: String, authorization: String): RoutingHttpResponse {
            urls += url
            authorizations += authorization
            return response
        }
    }

    private fun provider(transport: GeocodingHttpTransport) =
        OrsGeocodingProvider("secret", transport, "https://example.test/geocode")

    @Test fun autocompleteBuildsEncodedUsBoundedRequestWithFocus() = runTest {
        val transport = RecordingTransport(RoutingHttpResponse(200, featureCollection))
        provider(transport).autocomplete("123 Main St, Scranton", GeoPoint(41.4, -75.66))

        val url = transport.urls.single()
        assertTrue(url.startsWith("https://example.test/geocode/autocomplete?"))
        assertTrue(url.contains("text=123+Main+St%2C+Scranton"))
        assertTrue(url.contains("boundary.country=US"))
        assertTrue(url.contains("size=6"))
        assertTrue(url.contains("focus.point.lon=-75.66"))
        assertTrue(url.contains("focus.point.lat=41.4"))
        assertEquals("secret", transport.authorizations.single())
    }

    @Test fun autocompleteParsesLabelsAndCoordinatesAndDropsInvalidFeatures() = runTest {
        val result = provider(RecordingTransport(RoutingHttpResponse(200, featureCollection)))
            .autocomplete("Scranton")

        val suggestions = (result as GeocodeLookupResult.Success).suggestions
        assertEquals(
            listOf(GeocodeSuggestion("Scranton, PA, USA", GeoPoint(41.408, -75.662))),
            suggestions,
        )
    }

    @Test fun emptyFeatureListIsSuccessWithNoSuggestions() = runTest {
        val body = """{"type":"FeatureCollection","features":[]}"""
        val result = provider(RecordingTransport(RoutingHttpResponse(200, body)))
            .autocomplete("Scranton")
        assertTrue((result as GeocodeLookupResult.Success).suggestions.isEmpty())
    }

    @Test fun shortAutocompleteTextShortCircuitsWithoutRequest() = runTest {
        val transport = RecordingTransport(RoutingHttpResponse(200, featureCollection))
        val result = provider(transport).autocomplete("Sc")
        assertTrue((result as GeocodeLookupResult.Success).suggestions.isEmpty())
        assertTrue(transport.urls.isEmpty())
    }

    @Test fun missingKeyFailsWithConfigurationMessageWithoutRequest() = runTest {
        val transport = RecordingTransport(RoutingHttpResponse(200, featureCollection))
        val geocoder = OrsGeocodingProvider(" ", transport, "https://example.test/geocode")

        val autocomplete = geocoder.autocomplete("Scranton") as GeocodeLookupResult.Failure
        val reverse = geocoder.reverse(GeoPoint(41.4, -75.66)) as GeocodeLookupResult.Failure

        assertEquals(
            "OpenRouteService is not configured. Set ORS_API_KEY locally and rebuild.",
            autocomplete.message,
        )
        assertEquals(autocomplete.message, reverse.message)
        assertTrue(transport.urls.isEmpty())
    }

    @Test fun rateLimitIsRetryableFailureWithStatus() = runTest {
        val result = provider(
            RecordingTransport(RoutingHttpResponse(429, """{"error":"Rate limit exceeded"}""")),
        ).autocomplete("Scranton") as GeocodeLookupResult.Failure

        assertEquals(429, result.httpStatus)
        assertTrue(result.retryable)
        assertEquals("Rate limit exceeded", result.message)
    }

    @Test fun objectFormProviderErrorMessageIsSurfaced() = runTest {
        val result = provider(
            RecordingTransport(RoutingHttpResponse(403, """{"error":{"message":"Access to this API has been disallowed"}}""")),
        ).autocomplete("Scranton") as GeocodeLookupResult.Failure

        assertEquals(403, result.httpStatus)
        assertEquals("Access to this API has been disallowed", result.message)
    }

    @Test fun malformedBodyIsExplicitFailure() = runTest {
        val result = provider(RecordingTransport(RoutingHttpResponse(200, "not json")))
            .autocomplete("Scranton")
        assertTrue(result is GeocodeLookupResult.Failure)
    }

    @Test fun reverseRequestsSingleResultAtExactPoint() = runTest {
        val transport = RecordingTransport(RoutingHttpResponse(200, featureCollection))
        provider(transport).reverse(GeoPoint(41.4, -75.66))

        val url = transport.urls.single()
        assertTrue(url.contains("/reverse?"))
        assertTrue(url.contains("point.lon=-75.66"))
        assertTrue(url.contains("point.lat=41.4"))
        assertTrue(url.contains("size=1"))
    }

    @Test fun reverseRejectsInvalidPointWithoutRequest() = runTest {
        val transport = RecordingTransport(RoutingHttpResponse(200, featureCollection))
        val result = provider(transport).reverse(GeoPoint(200.0, 500.0))
        assertTrue(result is GeocodeLookupResult.Failure)
        assertTrue(transport.urls.isEmpty())
    }

    // One valid feature, one with out-of-range coordinates, one with no label.
    private val featureCollection = """
        {
          "geocoding":{"version":"0.2"},
          "type":"FeatureCollection",
          "features":[
            {"type":"Feature",
             "geometry":{"type":"Point","coordinates":[-75.662,41.408]},
             "properties":{"id":"1","label":"Scranton, PA, USA"}},
            {"type":"Feature",
             "geometry":{"type":"Point","coordinates":[500.0,200.0]},
             "properties":{"id":"2","label":"Broken coordinates"}},
            {"type":"Feature",
             "geometry":{"type":"Point","coordinates":[-75.0,41.0]},
             "properties":{"id":"3"}}
          ]
        }
    """.trimIndent()
}
