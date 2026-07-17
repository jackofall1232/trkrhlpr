package com.lastwagon.feature.routing

import com.lastwagon.core.model.GeoPoint
import com.lastwagon.core.model.RouteCalculationResult
import com.lastwagon.core.model.RouteRequest
import com.lastwagon.core.model.RoutingProvider

/**
 * Resolves the OpenRouteService key per request: a non-blank driver-supplied override wins,
 * otherwise the build-time key applies. Lets a driver keep routing alive on their own quota
 * when the shared baked key is exhausted or rotated, without rebuilding the APK.
 */
internal fun effectiveOrsKey(override: String, buildTimeKey: String): String =
    override.trim().ifEmpty { buildTimeKey }

class KeyOverrideRoutingProvider(
    private val buildTimeKey: String,
    private val overrideKey: suspend () -> String,
    private val createDelegate: (String) -> RoutingProvider = { key -> OrsRoutingProvider(key) },
) : RoutingProvider {
    override val id = "openrouteservice"
    override suspend fun calculate(request: RouteRequest): RouteCalculationResult =
        createDelegate(effectiveOrsKey(overrideKey(), buildTimeKey)).calculate(request)
}

class KeyOverrideGeocodingProvider(
    private val buildTimeKey: String,
    private val overrideKey: suspend () -> String,
    private val createDelegate: (String) -> GeocodingProvider = { key -> OrsGeocodingProvider(key) },
) : GeocodingProvider {
    override val id = "openrouteservice-geocode"
    override suspend fun autocomplete(text: String, focus: GeoPoint?): GeocodeLookupResult =
        createDelegate(effectiveOrsKey(overrideKey(), buildTimeKey)).autocomplete(text, focus)
    override suspend fun reverse(point: GeoPoint): GeocodeLookupResult =
        createDelegate(effectiveOrsKey(overrideKey(), buildTimeKey)).reverse(point)
}
