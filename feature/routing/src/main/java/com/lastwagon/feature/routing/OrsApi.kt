package com.lastwagon.feature.routing

/**
 * Single source of truth for the hosted OpenRouteService entry points.
 *
 * HeiGIT deprecated `api.openrouteservice.org` in favour of the unified `api.heigit.org`
 * gateway (announcement 2026-04-28, ask.openrouteservice.org thread 7912). The
 * `/ors`-prefixed paths below follow the gateway's per-service namespacing. The exact path
 * mapping could not be re-verified against live ORS documentation from the restricted
 * build environment, so verify with one keyed request (see docs/routing-provider.md); if
 * the provider dashboard shows different example URLs, update [BASE_URL] here — every
 * hosted-ORS request in the app derives from it.
 */
internal object OrsApi {
    const val BASE_URL = "https://api.heigit.org/ors"
    const val DIRECTIONS_HGV_GEOJSON_URL = "$BASE_URL/v2/directions/driving-hgv/geojson"
    const val GEOCODE_BASE_URL = "$BASE_URL/geocode"
}
