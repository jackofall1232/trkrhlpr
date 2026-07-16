package com.lastwagon.feature.routing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MapStyleProviderTest {
    @Test
    fun demoProviderIsExplicitlyEvaluationOnlyAndNeverPrefetched() {
        val style = MapLibreDemoStyleProvider.style()

        assertEquals("maplibre-demo-world", style.id)
        assertTrue(style.styleUri.startsWith("https://"))
        assertTrue(style.attribution.isNotBlank())
        assertTrue(style.usageNotice.contains("not commercial-truck routing data"))
        assertFalse(style.offlinePrefetchPermitted)
    }

    @Test
    fun openFreeMapProviderPermitsCorridorPrefetchWithAttribution() {
        val style = OpenFreeMapLibertyStyleProvider.style()

        assertEquals("openfreemap-liberty", style.id)
        assertTrue(style.styleUri.startsWith("https://tiles.openfreemap.org/"))
        assertTrue(style.attribution.contains("OpenFreeMap"))
        assertTrue(style.attribution.contains("OpenStreetMap"))
        assertTrue(style.offlinePrefetchPermitted)
    }
}
