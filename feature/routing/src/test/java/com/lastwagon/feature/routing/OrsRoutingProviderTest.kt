package com.lastwagon.feature.routing

import com.lastwagon.core.model.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class OrsRoutingProviderTest {
    private val profile = VehicleProfile(
        CommercialVehicleType.TRACTOR_TRAILER, 4.1, 2.5, 21.0, 36.0, 9.0, 5,
        hazmat = true, avoidTolls = true, avoidFerries = true, avoidUnpavedRoads = true,
        confirmedAtEpochMillis = 1L,
    )
    private val request = RouteRequest(GeoPoint(40.0, -75.0), GeoPoint(41.0, -76.0), profile)

    @Test fun payloadContainsExactVehicleRestrictionsAndSupportedAvoidances() {
        val provider = provider(RoutingHttpResponse(200, successBody))
        val payload = Json.parseToJsonElement(provider.buildPayload(request)).toString()

        assertTrue(payload.contains("\"driving-hgv\"").not())
        assertTrue(payload.contains("\"height\":4.1"))
        assertTrue(payload.contains("\"weight\":36.0"))
        assertTrue(payload.contains("\"hazmat\":true"))
        assertTrue(payload.contains("\"tollways\""))
        assertTrue(payload.contains("\"ferries\""))
        assertFalse(payload.contains("unpaved"))
    }

    @Test fun parsesGeometrySummaryWarningsAndProvenance() = runTest {
        val result = provider(RoutingHttpResponse(200, successBody)).calculate(request)

        assertTrue(result is RouteCalculationResult.Success)
        val route = (result as RouteCalculationResult.Success).route
        assertEquals(2, route.geometry.size)
        assertEquals(1234.5, route.distanceMeters, 0.001)
        assertEquals(1, route.steps.size)
        assertEquals(1, route.roadAccessRestrictionSegments)
        assertTrue(route.warnings.any { it.contains("unpaved-road") })
        assertEquals("request-1", route.provenance.requestId)
        assertEquals("openrouteservice", route.provenance.providerId)
        assertEquals(64, route.provenance.responseSha256.length)
        assertFalse(route.provenance.requestPayload.contains("secret"))
    }

    @Test fun missingKeyAndRateLimitAreExplicitFailures() = runTest {
        val missing = OrsRoutingProvider(" ").calculate(request)
        assertEquals(RouteFailureKind.MISSING_CREDENTIAL,
            (missing as RouteCalculationResult.Failure).error.kind)

        val limited = provider(RoutingHttpResponse(429, """{"error":{"message":"quota"}}"""))
            .calculate(request) as RouteCalculationResult.Failure
        assertEquals(RouteFailureKind.RATE_LIMITED, limited.error.kind)
        assertTrue(limited.error.retryable)
    }

    private fun provider(response: RoutingHttpResponse) = OrsRoutingProvider(
        "secret", RoutingHttpTransport { _, _, _ -> response }, "https://example.test/ors",
        { 100L }, { "request-1" },
    )

    private val successBody = """
        {
          "type":"FeatureCollection",
          "features":[{
            "type":"Feature",
            "geometry":{"type":"LineString","coordinates":[[-75.0,40.0],[-76.0,41.0]]},
            "properties":{
              "summary":{"distance":1234.5,"duration":345.6},
              "segments":[{"steps":[{"instruction":"Head north","distance":1234.5,"duration":345.6}]}],
              "extras":{"roadaccessrestrictions":{"values":[[0,1,0]]}}
            }
          }],
          "metadata":{
            "attribution":"openrouteservice.org, OpenStreetMap contributors",
            "service":"routing","timestamp":99,
            "engine":{"version":"9","build_date":"2026-01-01","graph_date":"2025-12-01"}
          }
        }
    """.trimIndent()
}
