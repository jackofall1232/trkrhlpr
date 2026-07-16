package com.lastwagon.feature.routing

import com.lastwagon.core.model.*
import org.junit.Assert.*
import org.junit.Test

class OfflineCorridorTest {
    private val profile = VehicleProfile(
        CommercialVehicleType.TRACTOR_TRAILER, 4.1, 2.5, 21.0, 36.0, 9.0, 5,
        false, true, true, false, 1L,
    )
    private val route = CalculatedRoute(
        RouteRequest(GeoPoint(40.0, -75.0), GeoPoint(41.0, -76.0), profile),
        listOf(GeoPoint(40.0, -75.0), GeoPoint(41.0, -76.0)),
        1000.0, 200.0, listOf(RouteStep("Continue", 1000.0, 200.0)),
        emptyList(), 0,
        RouteProvenance(
            "request-1", "openrouteservice", "https://example.test", "driving-hgv",
            10L, 20L, "{\"redacted\":true}", "a".repeat(64), "attribution", "routing",
            15L, "9", "build", "graph",
        ),
        review = RouteReview("request-1", 1L, 30L),
    )
    private val metadata = OfflineCorridor.metadataFor(
        route, "openfreemap-liberty", CorridorDetail.STANDARD, nowEpochMillis = 1_000L,
    )

    @Test fun metadataRoundTrips() {
        assertEquals(metadata, OfflineCorridor.decodeMetadata(OfflineCorridor.encodeMetadata(metadata)))
    }

    @Test fun corruptAndUnknownSchemaMetadataIsRejected() {
        assertNull(OfflineCorridor.decodeMetadata(byteArrayOf(1, 2, 3)))
        assertNull(OfflineCorridor.decodeMetadata("{}".encodeToByteArray()))
        val unknownSchema = OfflineCorridor.encodeMetadata(metadata).decodeToString()
            .replace("\"schema_version\":1", "\"schema_version\":99")
        assertNull(OfflineCorridor.decodeMetadata(unknownSchema.encodeToByteArray()))
    }

    @Test fun corridorMatchesOnlyTheExactRouteAndProfile() {
        assertTrue(OfflineCorridor.matches(metadata, route))
        assertFalse(OfflineCorridor.matches(metadata, null))
        assertFalse(OfflineCorridor.matches(metadata.copy(routeRequestId = "other"), route))
        val reconfirmedProfile = route.copy(
            request = route.request.copy(vehicleProfile = profile.copy(confirmedAtEpochMillis = 2L)),
        )
        assertFalse(OfflineCorridor.matches(metadata, reconfirmedProfile))
    }

    @Test fun corridorExpiresAfterTheConfiguredWindow() {
        assertFalse(OfflineCorridor.isExpired(metadata, metadata.createdAtEpochMillis))
        val justBefore = metadata.expiresAtEpochMillis - 1
        assertFalse(OfflineCorridor.isExpired(metadata, justBefore))
        assertTrue(OfflineCorridor.isExpired(metadata, metadata.expiresAtEpochMillis))
        assertEquals(
            OfflineCorridor.EXPIRY_DAYS * 24 * 60 * 60 * 1000,
            metadata.expiresAtEpochMillis - metadata.createdAtEpochMillis,
        )
    }

    @Test fun downloadsAreRefusedWithoutReviewOrPrefetchPermission() {
        val prefetchStyle = OpenFreeMapLibertyStyleProvider.style()
        assertNull(OfflineCorridor.downloadRefusalReason(route, prefetchStyle))
        assertNotNull(OfflineCorridor.downloadRefusalReason(null, prefetchStyle))
        assertNotNull(OfflineCorridor.downloadRefusalReason(route.copy(review = null), prefetchStyle))
        assertNotNull(
            OfflineCorridor.downloadRefusalReason(route, MapLibreDemoStyleProvider.style()),
        )
    }

    @Test fun tileEstimateGrowsWithDetailAndStaysPlausible() {
        val geometry = listOf(
            GeoPoint(40.0, -75.0), GeoPoint(40.2, -75.4), GeoPoint(40.5, -75.9),
            GeoPoint(40.8, -76.3), GeoPoint(41.0, -76.8),
        )
        val overview = OfflineCorridor.estimateTileCount(geometry, CorridorDetail.OVERVIEW)
        val standard = OfflineCorridor.estimateTileCount(geometry, CorridorDetail.STANDARD)
        val detailed = OfflineCorridor.estimateTileCount(geometry, CorridorDetail.DETAILED)

        assertTrue(overview > 0)
        assertTrue(standard > overview)
        assertTrue(detailed > standard)
        // A ~170 km route corridor must stay well inside the safety cap even when detailed.
        assertTrue(detailed < OfflineCorridor.MAX_TILES)
    }

    @Test fun tileEstimateIsEmptyForEmptyGeometry() {
        assertEquals(0L, OfflineCorridor.estimateTileCount(emptyList(), CorridorDetail.STANDARD))
    }

    @Test fun offCorridorRequiresConfidenceBeyondReportedAccuracy() {
        assertFalse(OfflineCorridor.isOffCorridor(2_999.0, accuracyMeters = null))
        assertTrue(OfflineCorridor.isOffCorridor(3_001.0, accuracyMeters = null))
        // A coarse fix cannot prove the driver left the corridor.
        assertFalse(OfflineCorridor.isOffCorridor(4_000.0, accuracyMeters = 2_000.0))
        assertTrue(OfflineCorridor.isOffCorridor(6_000.0, accuracyMeters = 2_000.0))
    }
}
