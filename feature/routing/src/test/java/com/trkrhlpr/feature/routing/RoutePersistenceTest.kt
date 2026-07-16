package com.trkrhlpr.feature.routing

import com.trkrhlpr.core.model.*
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
        assertNull(decodeRoute("""{"schema_version":2}"""))
        val corrupt = encodeRoute(route).replace("\"latitude\":40.0", "\"latitude\":400.0")
        assertNull(decodeRoute(corrupt))
    }
}
