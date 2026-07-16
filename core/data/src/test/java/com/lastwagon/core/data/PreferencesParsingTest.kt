package com.lastwagon.core.data

import com.lastwagon.core.model.ThemePreference
import org.junit.Assert.assertEquals
import org.junit.Test

class PreferencesParsingTest {
    @Test fun validThemeIsRestored() {
        assertEquals(ThemePreference.LIGHT, parseThemePreference("LIGHT"))
    }

    @Test fun unknownOrMissingThemeFallsBackToDark() {
        assertEquals(ThemePreference.DARK, parseThemePreference("RENAMED_VALUE"))
        assertEquals(ThemePreference.DARK, parseThemePreference(null))
    }
}
