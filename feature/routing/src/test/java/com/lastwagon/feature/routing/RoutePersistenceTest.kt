package com.lastwagon.feature.routing

import com.lastwagon.core.model.*
import org.junit.Assert.*
import org.junit.Test

class RoutePersistenceTest {
    private val profile = VehicleProfile(
        CommercialVehicleType.TRACTOR_TRAILER, 4.1, 2.5, 21.0, 36.0, 9.0, 5,
        false, true, true, false, 1L,
    )
    private val route = CalculatedRoute(
        RouteRequest(GeoPoint(40.0, -75.0), GeoPoint(41.0, -76.0), profile),
        listOf(GeoPoint(40.0, -75.0), GeoPoint(41.0, -76.0)),
        1000.0, 200.0, listOf(RouteStep("Continue", 1000.0, 200.0)),
        listOf("Driver verification required"), 1,
        RouteProvenance(
            "request-1", "openrouteservice", "https://example.test", "driving-hgv",
            10L, 20L, "{\"redacted\":true}", "a".repeat(64), "attribution", "routing",
            15L, "9", "build", "graph",
        ),
    )

    @Test fun routeRoundTripsWithRequestGeometryAndProvenance() {
        val decoded = decodeRoute(encodeRoute(route))

        assertEquals(route, decoded)
    }

    @Test fun unknownSchemaAndCorruptGeometryAreRejected() {
        assertNull(decodeRoute("""{"schema_version":99}"""))
        val corrupt = encodeRoute(route).replace("\"latitude\":40.0", "\"latitude\":400.0")
        assertNull(decodeRoute(corrupt))
    }

    @Test fun reviewedRouteRoundTripsAndMismatchedReviewIsRejected() {
        val reviewed = route.copy(review = RouteReview("request-1", 1L, 30L))
        assertEquals(reviewed, decodeRoute(encodeRoute(reviewed)))

        val mismatched = route.copy(review = RouteReview("different", 1L, 30L))
        assertNull(decodeRoute(encodeRoute(mismatched)))
    }

    @Test fun phaseThreeSchemaLoadsAsUnreviewed() {
        val phaseThree = encodeRoute(route).replace("\"schema_version\":2", "\"schema_version\":1")
        val decoded = decodeRoute(phaseThree)

        assertNotNull(decoded)
        assertNull(decoded?.review)
        assertFalse(decoded?.hasCurrentDriverReview == true)
        assertFalse(canOpenRouteMap(decoded))
    }

    @Test fun mapGateRequiresReviewForExactRouteAndProfile() {
        assertFalse(canOpenRouteMap(route))
        assertTrue(canOpenRouteMap(route.copy(review = RouteReview("request-1", 1L, 30L))))
        assertFalse(canOpenRouteMap(route.copy(review = RouteReview("wrong", 1L, 30L))))
        assertFalse(canOpenRouteMap(route.copy(review = RouteReview("request-1", 2L, 30L))))
    }
}
