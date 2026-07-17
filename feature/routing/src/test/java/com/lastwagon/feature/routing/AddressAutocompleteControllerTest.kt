package com.lastwagon.feature.routing

import com.lastwagon.core.model.GeoPoint
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class AddressAutocompleteControllerTest {
    private class RecordingGeocoder(
        private val result: (String) -> GeocodeLookupResult,
    ) : GeocodingProvider {
        override val id = "fake"
        val queries = mutableListOf<String>()
        override suspend fun autocomplete(text: String, focus: GeoPoint?): GeocodeLookupResult {
            queries += text
            return result(text)
        }

        override suspend fun reverse(point: GeoPoint) = result("reverse")
    }

    private val suggestion = GeocodeSuggestion("Scranton, PA, USA", GeoPoint(41.408, -75.662))

    @Test fun rapidTypingSendsExactlyOneRequestForTheFinalText() = runTest {
        val geocoder = RecordingGeocoder { GeocodeLookupResult.Success(listOf(suggestion)) }
        val controller = AddressAutocompleteController(geocoder, this, debounceMillis = 450L)

        listOf("Scr", "Scra", "Scran", "Scrant", "Scranton").forEach { typed ->
            controller.onQueryChanged(typed)
            advanceTimeBy(100L)
        }
        advanceTimeBy(451L)
        runCurrent()

        assertEquals(listOf("Scranton"), geocoder.queries)
        assertEquals(AutocompleteState.Suggestions(listOf(suggestion)), controller.state.value)
    }

    @Test fun queriesShorterThanMinimumNeverCallTheProvider() = runTest {
        val geocoder = RecordingGeocoder { GeocodeLookupResult.Success(listOf(suggestion)) }
        val controller = AddressAutocompleteController(geocoder, this, debounceMillis = 450L)

        controller.onQueryChanged("Sc")
        advanceTimeBy(10_000L)
        runCurrent()

        assertTrue(geocoder.queries.isEmpty())
        assertEquals(AutocompleteState.Idle, controller.state.value)
    }

    @Test fun emptyResultsBecomeNoMatchesAndFailuresSurfaceTheMessage() = runTest {
        val geocoder = RecordingGeocoder { text ->
            if (text == "nowhere at all") GeocodeLookupResult.Success(emptyList())
            else GeocodeLookupResult.Failure("service down")
        }
        val controller = AddressAutocompleteController(geocoder, this, debounceMillis = 450L)

        controller.onQueryChanged("nowhere at all")
        advanceTimeBy(451L)
        runCurrent()
        assertEquals(AutocompleteState.NoMatches, controller.state.value)

        controller.onQueryChanged("broken query")
        advanceTimeBy(451L)
        runCurrent()
        assertEquals(AutocompleteState.Failed("service down"), controller.state.value)
    }

    @Test fun clearingBeforeTheDebounceElapsesCancelsThePendingRequest() = runTest {
        val geocoder = RecordingGeocoder { GeocodeLookupResult.Success(listOf(suggestion)) }
        val controller = AddressAutocompleteController(geocoder, this, debounceMillis = 450L)

        controller.onQueryChanged("Scranton")
        advanceTimeBy(200L)
        controller.clear()
        advanceTimeBy(10_000L)
        runCurrent()

        assertTrue(geocoder.queries.isEmpty())
        assertEquals(AutocompleteState.Idle, controller.state.value)
    }

    @Test fun shorteningBelowMinimumCancelsThePendingRequest() = runTest {
        val geocoder = RecordingGeocoder { GeocodeLookupResult.Success(listOf(suggestion)) }
        val controller = AddressAutocompleteController(geocoder, this, debounceMillis = 450L)

        controller.onQueryChanged("Scranton")
        advanceTimeBy(200L)
        controller.onQueryChanged("Sc")
        advanceTimeBy(10_000L)
        runCurrent()

        assertTrue(geocoder.queries.isEmpty())
        assertEquals(AutocompleteState.Idle, controller.state.value)
    }
}
