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

/** Caches a delegate per effective key so steady-state requests reuse one instance. */
private class DelegateCache<T>(private val create: (String) -> T) {
    private var key: String? = null
    private var delegate: T? = null

    fun forKey(effectiveKey: String): T = synchronized(this) {
        val current = delegate
        if (current != null && key == effectiveKey) return current
        create(effectiveKey).also { created ->
            key = effectiveKey
            delegate = created
        }
    }
}

class KeyOverrideRoutingProvider(
    private val buildTimeKey: String,
    private val overrideKey: suspend () -> String,
    createDelegate: (String) -> RoutingProvider = { key -> OrsRoutingProvider(key) },
) : RoutingProvider {
    private val cache = DelegateCache(createDelegate)
    override val id = "openrouteservice"
    override suspend fun calculate(request: RouteRequest): RouteCalculationResult =
        cache.forKey(effectiveOrsKey(overrideKey(), buildTimeKey)).calculate(request)
}

class KeyOverrideGeocodingProvider(
    private val buildTimeKey: String,
    private val overrideKey: suspend () -> String,
    createDelegate: (String) -> GeocodingProvider = { key -> OrsGeocodingProvider(key) },
) : GeocodingProvider {
    private val cache = DelegateCache(createDelegate)
    override val id = "openrouteservice-geocode"
    override suspend fun autocomplete(text: String, focus: GeoPoint?): GeocodeLookupResult =
        cache.forKey(effectiveOrsKey(overrideKey(), buildTimeKey)).autocomplete(text, focus)
    override suspend fun reverse(point: GeoPoint): GeocodeLookupResult =
        cache.forKey(effectiveOrsKey(overrideKey(), buildTimeKey)).reverse(point)
}
