package com.lastwagon.feature.routing

import com.lastwagon.core.model.GeoPoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GeoMathTest {
    @Test fun oneDegreeOfLatitudeIsAbout111Kilometers() {
        val distance = GeoMath.distanceMeters(GeoPoint(40.0, -75.0), GeoPoint(41.0, -75.0))
        assertEquals(111_195.0, distance, 500.0)
    }

    @Test fun pointOnThePolylineHasNearZeroDistance() {
        val line = listOf(GeoPoint(40.0, -75.0), GeoPoint(40.0, -74.0))
        val distance = GeoMath.distanceToPolylineMeters(GeoPoint(40.0, -74.5), line)
        assertTrue("expected ~0, was $distance", distance < 1.0)
    }

    @Test fun pointBesideTheSegmentUsesPerpendicularDistance() {
        val line = listOf(GeoPoint(40.0, -75.0), GeoPoint(40.0, -74.0))
        // ~0.01 degrees of latitude north of the line is ~1112 meters away.
        val distance = GeoMath.distanceToPolylineMeters(GeoPoint(40.01, -74.5), line)
        assertEquals(1_112.0, distance, 25.0)
    }

    @Test fun pointPastTheEndUsesEndpointDistance()  {
        val line = listOf(GeoPoint(40.0, -75.0), GeoPoint(40.0, -74.0))
        val beyondEnd = GeoPoint(40.0, -73.9)
        val viaSegment = GeoMath.distanceToPolylineMeters(beyondEnd, line)
        val toEndpoint = GeoMath.distanceMeters(beyondEnd, GeoPoint(40.0, -74.0))
        assertEquals(toEndpoint, viaSegment, toEndpoint * 0.01)
    }

    @Test fun closestSegmentOfAMultiSegmentLineWins() {
        val line = listOf(
            GeoPoint(40.0, -75.0), GeoPoint(40.0, -74.0), GeoPoint(41.0, -74.0),
        )
        val nearSecondSegment = GeoPoint(40.5, -74.01)
        val distance = GeoMath.distanceToPolylineMeters(nearSecondSegment, line)
        assertTrue("expected under 1500 m, was $distance", distance < 1_500.0)
    }

    @Test fun singlePointPolylineFallsBackToDirectDistance() {
        val distance = GeoMath.distanceToPolylineMeters(
            GeoPoint(40.0, -75.0), listOf(GeoPoint(41.0, -75.0)),
        )
        assertEquals(111_195.0, distance, 500.0)
    }
}
