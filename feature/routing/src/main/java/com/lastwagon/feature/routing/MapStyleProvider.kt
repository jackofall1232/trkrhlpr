package com.lastwagon.feature.routing

data class MapStyleDescriptor(
    val id: String,
    val styleUri: String,
    val attribution: String,
    val usageNotice: String,
    val offlinePrefetchPermitted: Boolean = false,
)

fun interface MapStyleProvider {
    fun style(): MapStyleDescriptor
}

/**
 * Token-free MapLibre project data for development and evaluation only.
 *
 * This is deliberately replaceable and is not approval of a production tile provider.
 * Corridor pre-fetching must never use this endpoint; `offlinePrefetchPermitted` stays
 * false and the corridor manager refuses providers that do not opt in.
 */
object MapLibreDemoStyleProvider : MapStyleProvider {
    override fun style() = MapStyleDescriptor(
        id = "maplibre-demo-world",
        styleUri = "https://demotiles.maplibre.org/style.json",
        attribution = "MapLibre demo map · Natural Earth contributors",
        usageNotice = "Evaluation map only — not commercial-truck routing data",
        offlinePrefetchPermitted = false,
    )
}

/**
 * OpenFreeMap Liberty style for Phase 5 development and device testing. Terms research,
 * safeguards, and the outstanding production confirmation are recorded in
 * `docs/map-provider-evaluation.md`; corridor downloads stay bounded by
 * [OfflineCorridor.MAX_TILES].
 */
object OpenFreeMapLibertyStyleProvider : MapStyleProvider {
    override fun style() = MapStyleDescriptor(
        id = "openfreemap-liberty",
        styleUri = "https://tiles.openfreemap.org/styles/liberty",
        attribution = "OpenFreeMap © OpenMapTiles · Data from OpenStreetMap",
        usageNotice = "General street map — not commercial-truck restriction data",
        offlinePrefetchPermitted = true,
    )
}
