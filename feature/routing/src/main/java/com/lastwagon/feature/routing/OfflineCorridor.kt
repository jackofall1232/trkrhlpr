package com.lastwagon.feature.routing

import com.lastwagon.core.model.CalculatedRoute
import com.lastwagon.core.model.GeoPoint
import com.lastwagon.core.model.hasCurrentDriverReview
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.put
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.tan

/**
 * Corridor download detail levels. Zoom ranges are deliberately small so a single route
 * corridor stays far below bulk-mirroring magnitude on the public tile instance.
 */
enum class CorridorDetail(val displayName: String, val minZoom: Double, val maxZoom: Double) {
    OVERVIEW("Overview — least storage", 0.0, 9.0),
    STANDARD("Standard", 0.0, 11.0),
    DETAILED("Detailed — most storage", 0.0, 12.0),
}

/** Identity and freshness of one downloaded corridor, stored in the offline region. */
data class CorridorMetadata(
    val routeRequestId: String,
    val vehicleProfileConfirmedAtEpochMillis: Long,
    val styleId: String,
    val detail: CorridorDetail,
    val createdAtEpochMillis: Long,
    val expiresAtEpochMillis: Long,
)

/** Visible corridor lifecycle used by the routing UI. */
sealed interface CorridorState {
    data object None : CorridorState
    data object Preparing : CorridorState
    data class Downloading(
        val metadata: CorridorMetadata,
        val completedResources: Long,
        val requiredResources: Long,
        val completedBytes: Long,
        val requiredCountIsPrecise: Boolean,
    ) : CorridorState

    data class Ready(
        val metadata: CorridorMetadata,
        val completedBytes: Long,
        val completedTiles: Long,
    ) : CorridorState

    data class Failed(val message: String) : CorridorState
}

object OfflineCorridor {
    const val SCHEMA_VERSION = 1

    /** Hard cap on tiles per corridor; also enforced natively via the tile count limit. */
    const val MAX_TILES = 6_000L

    /** Corridor map data is treated as stale after this many days. */
    const val EXPIRY_DAYS = 7L

    /**
     * Beyond this distance from the reviewed route line, an approximate position is
     * treated as outside the saved corridor. Coarse-location error is subtracted by the
     * caller through [isOffCorridor] so the warning fails toward silence only when the
     * position itself is too imprecise to judge.
     */
    const val OFF_CORRIDOR_THRESHOLD_METERS = 3_000.0

    fun metadataFor(
        route: CalculatedRoute,
        styleId: String,
        detail: CorridorDetail,
        nowEpochMillis: Long,
    ) = CorridorMetadata(
        routeRequestId = route.provenance.requestId,
        vehicleProfileConfirmedAtEpochMillis = route.request.vehicleProfile.confirmedAtEpochMillis,
        styleId = styleId,
        detail = detail,
        createdAtEpochMillis = nowEpochMillis,
        expiresAtEpochMillis = nowEpochMillis + EXPIRY_DAYS * 24 * 60 * 60 * 1000,
    )

    fun encodeMetadata(metadata: CorridorMetadata): ByteArray = buildJsonObject {
        put("schema_version", SCHEMA_VERSION)
        put("route_request_id", metadata.routeRequestId)
        put("vehicle_confirmed_at", metadata.vehicleProfileConfirmedAtEpochMillis)
        put("style_id", metadata.styleId)
        put("detail", metadata.detail.name)
        put("created_at", metadata.createdAtEpochMillis)
        put("expires_at", metadata.expiresAtEpochMillis)
    }.toString().encodeToByteArray()

    /** Returns null for corrupt bytes or an unknown schema so callers fail closed. */
    fun decodeMetadata(bytes: ByteArray): CorridorMetadata? = runCatching {
        val root = Json.parseToJsonElement(bytes.decodeToString()).jsonObject
        require(root.getValue("schema_version").jsonPrimitive.int == SCHEMA_VERSION)
        CorridorMetadata(
            routeRequestId = root.getValue("route_request_id").jsonPrimitive.content,
            vehicleProfileConfirmedAtEpochMillis = root.getValue("vehicle_confirmed_at").jsonPrimitive.long,
            styleId = root.getValue("style_id").jsonPrimitive.content,
            detail = CorridorDetail.valueOf(root.getValue("detail").jsonPrimitive.content),
            createdAtEpochMillis = root.getValue("created_at").jsonPrimitive.long,
            expiresAtEpochMillis = root.getValue("expires_at").jsonPrimitive.long,
        )
    }.getOrNull()

    /**
     * A corridor is only valid for the exact reviewed route, vehicle profile, and active
     * map style. A corridor downloaded for another style holds the wrong style and tile
     * resources, so it must be discarded rather than reported as offline coverage.
     */
    fun matches(metadata: CorridorMetadata, route: CalculatedRoute?, activeStyleId: String): Boolean =
        route != null &&
            metadata.styleId == activeStyleId &&
            metadata.routeRequestId == route.provenance.requestId &&
            metadata.vehicleProfileConfirmedAtEpochMillis ==
            route.request.vehicleProfile.confirmedAtEpochMillis

    fun isExpired(metadata: CorridorMetadata, nowEpochMillis: Long): Boolean =
        nowEpochMillis >= metadata.expiresAtEpochMillis

    /** Prefetching is refused unless the style provider explicitly permits it. */
    fun downloadRefusalReason(route: CalculatedRoute?, style: MapStyleDescriptor): String? = when {
        route == null -> "No saved route is available to download a corridor for."
        !route.hasCurrentDriverReview ->
            "Complete the mandatory driver review before downloading an offline corridor."
        !style.offlinePrefetchPermitted ->
            "The current map provider does not permit offline corridor downloads."
        else -> null
    }

    /**
     * Estimates the number of distinct tiles a corridor download will cover by walking
     * the route line at every zoom level and counting the tiles it crosses. This mirrors
     * how the offline geometry region selects tiles and is used to refuse oversized
     * downloads before any request is made.
     */
    fun estimateTileCount(geometry: List<GeoPoint>, detail: CorridorDetail): Long {
        if (geometry.isEmpty()) return 0
        var total = 0L
        var zoom = detail.minZoom.toInt()
        while (zoom <= detail.maxZoom.toInt()) {
            total += tilesCrossedAtZoom(geometry, zoom)
            zoom++
        }
        return total
    }

    private fun tilesCrossedAtZoom(geometry: List<GeoPoint>, zoom: Int): Long {
        val tiles = HashSet<Long>()
        val scale = 1 shl zoom
        var previous = tileCoordinate(geometry.first(), scale)
        addTile(tiles, previous, scale)
        for (index in 1 until geometry.size) {
            val next = tileCoordinate(geometry[index], scale)
            // Sample along the segment densely enough to touch every crossed tile.
            val steps = max(1, ceil(max(kotlin.math.abs(next.first - previous.first),
                kotlin.math.abs(next.second - previous.second)) * 2).toInt())
            for (step in 1..steps) {
                val t = step.toDouble() / steps
                addTile(tiles, Pair(
                    previous.first + (next.first - previous.first) * t,
                    previous.second + (next.second - previous.second) * t,
                ), scale)
            }
            previous = next
        }
        return tiles.size.toLong()
    }

    private fun tileCoordinate(point: GeoPoint, scale: Int): Pair<Double, Double> {
        val x = (point.longitude + 180.0) / 360.0 * scale
        val latRad = point.latitude * PI / 180.0
        val y = (1.0 - ln(tan(latRad) + 1 / kotlin.math.cos(latRad)) / PI) / 2.0 * scale
        return Pair(x, y)
    }

    private fun addTile(tiles: HashSet<Long>, coordinate: Pair<Double, Double>, scale: Int) {
        val x = floor(coordinate.first).toLong().coerceIn(0, scale - 1L)
        val y = floor(coordinate.second).toLong().coerceIn(0, scale - 1L)
        tiles.add(x * 4_294_967_296L + y)
    }

    /**
     * True when an approximate position is confidently outside the corridor: even after
     * subtracting the reported position error, the distance to the route line exceeds
     * the corridor threshold. Unknown accuracy is treated as zero error.
     */
    fun isOffCorridor(distanceToRouteMeters: Double, accuracyMeters: Double?): Boolean =
        distanceToRouteMeters - (accuracyMeters ?: 0.0) > OFF_CORRIDOR_THRESHOLD_METERS
}
