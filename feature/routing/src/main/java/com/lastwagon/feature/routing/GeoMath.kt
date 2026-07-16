package com.lastwagon.feature.routing

import com.lastwagon.core.model.GeoPoint
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Distance helpers for the off-corridor check. Segment distances use a local
 * equirectangular projection, which is accurate to well under a percent at the
 * few-kilometre scale the corridor threshold operates on.
 */
object GeoMath {
    const val EARTH_RADIUS_METERS = 6_371_008.8

    fun distanceMeters(a: GeoPoint, b: GeoPoint): Double {
        val phi1 = a.latitude.toRadians()
        val phi2 = b.latitude.toRadians()
        val deltaPhi = (b.latitude - a.latitude).toRadians()
        val deltaLambda = (b.longitude - a.longitude).toRadians()
        val h = sin(deltaPhi / 2) * sin(deltaPhi / 2) +
            cos(phi1) * cos(phi2) * sin(deltaLambda / 2) * sin(deltaLambda / 2)
        return 2 * EARTH_RADIUS_METERS * atan2(sqrt(h), sqrt(1 - h))
    }

    fun distanceToPolylineMeters(point: GeoPoint, polyline: List<GeoPoint>): Double {
        require(polyline.isNotEmpty()) { "Polyline must contain at least one point" }
        if (polyline.size == 1) return distanceMeters(point, polyline.first())
        var minimum = Double.MAX_VALUE
        for (index in 0 until polyline.size - 1) {
            minimum = minOf(
                minimum,
                distanceToSegmentMeters(point, polyline[index], polyline[index + 1]),
            )
        }
        return minimum
    }

    internal fun distanceToSegmentMeters(point: GeoPoint, start: GeoPoint, end: GeoPoint): Double {
        val cosLat = cos(point.latitude.toRadians())
        val px = point.longitude.toRadians() * cosLat
        val py = point.latitude.toRadians()
        val ax = start.longitude.toRadians() * cosLat
        val ay = start.latitude.toRadians()
        val bx = end.longitude.toRadians() * cosLat
        val by = end.latitude.toRadians()

        val abx = bx - ax
        val aby = by - ay
        val squaredLength = abx * abx + aby * aby
        val t = if (squaredLength == 0.0) 0.0 else
            (((px - ax) * abx + (py - ay) * aby) / squaredLength).coerceIn(0.0, 1.0)
        val dx = px - (ax + t * abx)
        val dy = py - (ay + t * aby)
        return sqrt(dx * dx + dy * dy) * EARTH_RADIUS_METERS
    }

    private fun Double.toRadians() = this * PI / 180.0
}
