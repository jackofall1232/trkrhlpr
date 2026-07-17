package com.lastwagon.feature.routing

import com.lastwagon.core.model.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class KeyOverrideProvidersTest {
    private val profile = VehicleProfile(
        CommercialVehicleType.TRACTOR_TRAILER, 4.1, 2.5, 21.0, 36.0, 9.0, 5,
        hazmat = false, avoidTolls = false, avoidFerries = false, avoidUnpavedRoads = false,
        confirmedAtEpochMillis = 1L,
    )
    private val request = RouteRequest(GeoPoint(40.0, -75.0), GeoPoint(41.0, -76.0), profile)

    @Test fun blankAndWhitespaceOverridesFallBackToBuildTimeKey() {
        assertEquals("baked", effectiveOrsKey("", "baked"))
        assertEquals("baked", effectiveOrsKey("   ", "baked"))
        assertEquals("driver", effectiveOrsKey(" driver ", "baked"))
    }

    @Test fun routingDelegateReceivesTheEffectiveKeyPerCall() = runTest {
        val keysUsed = mutableListOf<String>()
        var stored = ""
        val provider = KeyOverrideRoutingProvider(
            buildTimeKey = "baked",
            overrideKey = { stored },
            createDelegate = { key ->
                keysUsed += key
                object : RoutingProvider {
                    override val id = "fake"
                    override suspend fun calculate(request: RouteRequest) =
                        RouteCalculationResult.Failure(RouteFailure(RouteFailureKind.NETWORK, "n/a"))
                }
            },
        )
        provider.calculate(request)
        stored = "driver-key"
        provider.calculate(request)
        assertEquals(listOf("baked", "driver-key"), keysUsed)
    }

    @Test fun routingDelegateIsReusedWhileTheKeyIsUnchanged() = runTest {
        var creations = 0
        var stored = ""
        val provider = KeyOverrideRoutingProvider(
            buildTimeKey = "baked",
            overrideKey = { stored },
            createDelegate = {
                creations++
                object : RoutingProvider {
                    override val id = "fake"
                    override suspend fun calculate(request: RouteRequest) =
                        RouteCalculationResult.Failure(RouteFailure(RouteFailureKind.NETWORK, "n/a"))
                }
            },
        )
        provider.calculate(request)
        provider.calculate(request)
        assertEquals(1, creations)
        stored = "driver-key"
        provider.calculate(request)
        provider.calculate(request)
        assertEquals(2, creations)
    }

    @Test fun geocodingDelegateIsReusedWhileTheKeyIsUnchanged() = runTest {
        var creations = 0
        val provider = KeyOverrideGeocodingProvider(
            buildTimeKey = "baked",
            overrideKey = { "" },
            createDelegate = {
                creations++
                object : GeocodingProvider {
                    override val id = "fake"
                    override suspend fun autocomplete(text: String, focus: GeoPoint?) =
                        GeocodeLookupResult.Success(emptyList<GeocodeSuggestion>())
                    override suspend fun reverse(point: GeoPoint) =
                        GeocodeLookupResult.Success(emptyList<GeocodeSuggestion>())
                }
            },
        )
        provider.autocomplete("Scr")
        provider.autocomplete("Scranton")
        provider.reverse(GeoPoint(41.4, -75.66))
        assertEquals(1, creations)
    }

    @Test fun geocodingDelegateReceivesTheEffectiveKeyPerCall() = runTest {
        val keysUsed = mutableListOf<String>()
        var stored = "driver-key"
        val provider = KeyOverrideGeocodingProvider(
            buildTimeKey = "baked",
            overrideKey = { stored },
            createDelegate = { key ->
                keysUsed += key
                object : GeocodingProvider {
                    override val id = "fake"
                    override suspend fun autocomplete(text: String, focus: GeoPoint?) =
                        GeocodeLookupResult.Success(emptyList())
                    override suspend fun reverse(point: GeoPoint) =
                        GeocodeLookupResult.Success(emptyList())
                }
            },
        )
        provider.autocomplete("Scranton")
        stored = " "
        provider.reverse(GeoPoint(41.4, -75.66))
        assertEquals(listOf("driver-key", "baked"), keysUsed)
    }
}
