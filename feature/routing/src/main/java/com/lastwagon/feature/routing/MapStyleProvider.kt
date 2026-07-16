package com.lastwagon.feature.routing

data class MapStyleDescriptor(
    val id: String,
    val styleUri: String,
    val attribution: String,
    val usageNotice: String,
)

fun interface MapStyleProvider {
    fun style(): MapStyleDescriptor
}

/**
 * Token-free MapLibre project data for development and evaluation only.
 *
 * This is deliberately replaceable and is not approval of a production tile provider.
 * Phase 5 pre-fetching must not use this endpoint without a separate terms review.
 */
object MapLibreDemoStyleProvider : MapStyleProvider {
    override fun style() = MapStyleDescriptor(
        id = "maplibre-demo-world",
        styleUri = "https://demotiles.maplibre.org/style.json",
        attribution = "MapLibre demo map · Natural Earth contributors",
        usageNotice = "Evaluation map only — not commercial-truck routing data",
    )
}
