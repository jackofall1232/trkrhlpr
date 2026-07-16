package com.trkrhlpr.feature.routing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MapStyleProviderTest {
    @Test
    fun demoProviderIsExplicitlyEvaluationOnly() {
        val style = MapLibreDemoStyleProvider.style()

        assertEquals("maplibre-demo-world", style.id)
        assertTrue(style.styleUri.startsWith("https://"))
        assertTrue(style.attribution.isNotBlank())
        assertTrue(style.usageNotice.contains("not commercial-truck routing data"))
    }
}
