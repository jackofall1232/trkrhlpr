package com.lastwagon.feature.routing

import android.content.Context
import com.lastwagon.core.model.CalculatedRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.maplibre.android.MapLibre
import org.maplibre.android.offline.OfflineGeometryRegionDefinition
import org.maplibre.android.offline.OfflineManager
import org.maplibre.android.offline.OfflineRegion
import org.maplibre.android.offline.OfflineRegionError
import org.maplibre.android.offline.OfflineRegionStatus
import org.maplibre.geojson.LineString
import org.maplibre.geojson.Point

/**
 * Owns the single offline route corridor. All MapLibre offline callbacks arrive on the
 * main thread; state is published through [state] for the routing UI.
 *
 * Fail-safe rules: at most one corridor exists; a corridor that does not match the
 * current saved route (route replaced, deleted, or profile reconfirmed) is deleted; a
 * corridor with unreadable metadata is deleted; downloads are refused for providers
 * that do not permit prefetching and for unreviewed routes; nothing here ever
 * recalculates a route.
 */
class OfflineCorridorManager(
    context: Context,
    private val now: () -> Long = System::currentTimeMillis,
) {
    private val appContext = context.applicationContext
    private val _state = MutableStateFlow<CorridorState>(CorridorState.None)
    val state: StateFlow<CorridorState> = _state

    private var activeRegion: OfflineRegion? = null

    private val manager: OfflineManager by lazy {
        MapLibre.getInstance(appContext)
        OfflineManager.getInstance(appContext).also {
            it.setOfflineMapboxTileCountLimit(OfflineCorridor.MAX_TILES)
        }
    }

    /**
     * Reconciles stored corridors against the current saved route: adopts a matching
     * corridor (resuming an interrupted download), deletes everything else.
     */
    fun refresh(currentRoute: CalculatedRoute?) {
        manager.listOfflineRegions(object : OfflineManager.ListOfflineRegionsCallback {
            override fun onList(offlineRegions: Array<OfflineRegion>?) {
                val regions = offlineRegions.orEmpty()
                var adopted = false
                regions.forEach { region ->
                    val metadata = OfflineCorridor.decodeMetadata(region.metadata)
                    if (!adopted && metadata != null && OfflineCorridor.matches(metadata, currentRoute)) {
                        adopted = true
                        adopt(region, metadata)
                    } else {
                        discard(region)
                    }
                }
                if (!adopted) {
                    activeRegion = null
                    _state.value = CorridorState.None
                }
            }

            override fun onError(error: String) {
                _state.value = CorridorState.Failed(
                    "Saved offline corridors could not be read: $error",
                )
            }
        })
    }

    fun download(route: CalculatedRoute, style: MapStyleDescriptor, detail: CorridorDetail) {
        OfflineCorridor.downloadRefusalReason(route, style)?.let {
            _state.value = CorridorState.Failed(it)
            return
        }
        val estimatedTiles = OfflineCorridor.estimateTileCount(route.geometry, detail)
        if (estimatedTiles > OfflineCorridor.MAX_TILES) {
            _state.value = CorridorState.Failed(
                "This corridor would need about $estimatedTiles map tiles, above the " +
                    "${OfflineCorridor.MAX_TILES}-tile limit. Choose a lower detail level.",
            )
            return
        }
        _state.value = CorridorState.Preparing
        activeRegion = null
        val metadata = OfflineCorridor.metadataFor(route, style.id, detail, now())
        val definition = OfflineGeometryRegionDefinition(
            style.styleUri,
            LineString.fromLngLats(route.geometry.map { Point.fromLngLat(it.longitude, it.latitude) }),
            detail.minZoom,
            detail.maxZoom,
            appContext.resources.displayMetrics.density,
        )
        // Clear any previous corridor first so only one download ever exists.
        manager.listOfflineRegions(object : OfflineManager.ListOfflineRegionsCallback {
            override fun onList(offlineRegions: Array<OfflineRegion>?) {
                offlineRegions.orEmpty().forEach { discard(it) }
                create(definition, metadata)
            }

            override fun onError(error: String) {
                create(definition, metadata)
            }
        })
    }

    /** Cancels an in-progress download (or removes a finished corridor). */
    fun cancel() = delete()

    fun delete() {
        val region = activeRegion
        activeRegion = null
        if (region == null) {
            _state.value = CorridorState.None
            return
        }
        region.setDownloadState(OfflineRegion.STATE_INACTIVE)
        region.setObserver(null)
        region.delete(object : OfflineRegion.OfflineRegionDeleteCallback {
            override fun onDelete() {
                _state.value = CorridorState.None
            }

            override fun onError(error: String) {
                _state.value = CorridorState.Failed(
                    "The offline corridor could not be deleted: $error",
                )
            }
        })
    }

    private fun create(definition: OfflineGeometryRegionDefinition, metadata: CorridorMetadata) {
        manager.createOfflineRegion(
            definition,
            OfflineCorridor.encodeMetadata(metadata),
            object : OfflineManager.CreateOfflineRegionCallback {
                override fun onCreate(offlineRegion: OfflineRegion) {
                    adopt(offlineRegion, metadata)
                }

                override fun onError(error: String) {
                    _state.value = CorridorState.Failed(
                        "The offline corridor download could not start: $error",
                    )
                }
            },
        )
    }

    private fun adopt(region: OfflineRegion, metadata: CorridorMetadata) {
        activeRegion = region
        region.setObserver(object : OfflineRegion.OfflineRegionObserver {
            override fun onStatusChanged(status: OfflineRegionStatus) {
                if (activeRegion !== region) return
                _state.value = if (status.isComplete) {
                    CorridorState.Ready(metadata, status.completedResourceSize, status.completedTileCount)
                } else {
                    CorridorState.Downloading(
                        metadata,
                        status.completedResourceCount,
                        status.requiredResourceCount,
                        status.completedResourceSize,
                        status.isRequiredResourceCountPrecise,
                    )
                }
            }

            override fun onError(error: OfflineRegionError) {
                if (activeRegion !== region) return
                // Connection errors pause the download; it resumes when requested again.
                _state.value = CorridorState.Failed(
                    "Corridor download problem (${error.reason}): ${error.message}",
                )
            }

            override fun mapboxTileCountLimitExceeded(limit: Long) {
                if (activeRegion !== region) return
                region.setDownloadState(OfflineRegion.STATE_INACTIVE)
                _state.value = CorridorState.Failed(
                    "The corridor reached the $limit-tile safety limit before completing. " +
                        "Delete it and choose a lower detail level.",
                )
            }
        })
        region.setDownloadState(OfflineRegion.STATE_ACTIVE)
        region.getStatus(object : OfflineRegion.OfflineRegionStatusCallback {
            override fun onStatus(status: OfflineRegionStatus?) {
                if (activeRegion !== region || status == null) return
                _state.value = if (status.isComplete) {
                    CorridorState.Ready(metadata, status.completedResourceSize, status.completedTileCount)
                } else {
                    CorridorState.Downloading(
                        metadata,
                        status.completedResourceCount,
                        status.requiredResourceCount,
                        status.completedResourceSize,
                        status.isRequiredResourceCountPrecise,
                    )
                }
            }

            override fun onError(error: String?) = Unit
        })
    }

    private fun discard(region: OfflineRegion) {
        region.setDownloadState(OfflineRegion.STATE_INACTIVE)
        region.delete(object : OfflineRegion.OfflineRegionDeleteCallback {
            override fun onDelete() = Unit
            override fun onError(error: String) = Unit
        })
    }
}
