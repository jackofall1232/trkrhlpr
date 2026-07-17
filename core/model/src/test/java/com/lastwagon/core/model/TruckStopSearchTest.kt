package com.lastwagon.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class TruckStopSearchTest {
    private fun stop(
        id: String, name: String, state: String, highway: String = "I-1",
        diesel: Boolean? = null, showers: Boolean? = null,
        food: Boolean? = null, repair: Boolean? = null,
    ) = TruckStop(
        ContentId(id), name, state, highway, 40.0, -90.0,
        truckParkingSpaces = null, hasDiesel = diesel, hasShowers = showers,
        hasFood = food, hasRepair = repair,
    )

    private val stops = listOf(
        stop("a", "Alpha Plaza", "OH", "I-75 Exit 10", diesel = true, showers = true),
        stop("b", "Bravo Fuel", "OH", "I-70 Exit 20", diesel = true, showers = null),
        stop("c", "Charlie Rest", "IA", "I-80 Mile 30", diesel = null, showers = false),
    )

    @Test fun emptyFilterReturnsAllSortedByStateThenName() {
        val result = TruckStopSearch.search(stops, TruckStopFilter())
        assertEquals(listOf("c", "a", "b"), result.matches.map { it.id.value })
        assertEquals(0, result.hiddenUnknownCount)
    }

    @Test fun blankQueryMatchesEverything() {
        val result = TruckStopSearch.search(stops, TruckStopFilter(query = "   "))
        assertEquals(3, result.matches.size)
    }

    @Test fun queryMatchesNameHighwayAndStateCaseInsensitively() {
        assertEquals(listOf("a"),
            TruckStopSearch.search(stops, TruckStopFilter(query = "alpha")).matches.map { it.id.value })
        assertEquals(listOf("c"),
            TruckStopSearch.search(stops, TruckStopFilter(query = "i-80")).matches.map { it.id.value })
        assertEquals(listOf("c"),
            TruckStopSearch.search(stops, TruckStopFilter(query = "ia")).matches.map { it.id.value })
    }

    @Test fun stateFilterIsCaseInsensitive() {
        val result = TruckStopSearch.search(stops, TruckStopFilter(state = "oh"))
        assertEquals(listOf("a", "b"), result.matches.map { it.id.value })
    }

    @Test fun requiredAmenityKeepsOnlyRecordedTrueAndCountsUnknowns() {
        val result = TruckStopSearch.search(
            stops, TruckStopFilter(requiredAmenities = setOf(TruckStopAmenity.DIESEL)),
        )
        // c's diesel is unknown (null): hidden and counted, never treated as absent.
        assertEquals(listOf("a", "b"), result.matches.map { it.id.value })
        assertEquals(1, result.hiddenUnknownCount)
    }

    @Test fun amenityRecordedFalseIsAnOrdinaryNonMatchNotAHiddenUnknown() {
        val result = TruckStopSearch.search(
            stops, TruckStopFilter(requiredAmenities = setOf(TruckStopAmenity.SHOWERS)),
        )
        // b's showers are unknown -> hidden-unknown; c's showers are recorded false -> plain miss.
        assertEquals(listOf("a"), result.matches.map { it.id.value })
        assertEquals(1, result.hiddenUnknownCount)
    }

    @Test fun aRecordedFalseOutweighsAnUnknownOnTheSameStop() {
        val result = TruckStopSearch.search(
            stops,
            TruckStopFilter(requiredAmenities = setOf(TruckStopAmenity.DIESEL, TruckStopAmenity.SHOWERS)),
        )
        // c has diesel unknown AND showers false: the false makes it a plain non-match.
        assertEquals(listOf("a"), result.matches.map { it.id.value })
        assertEquals(1, result.hiddenUnknownCount) // only b (diesel true, showers unknown)
    }

    @Test fun hiddenUnknownIsNotCountedForStopsTheQueryAlreadyExcludes() {
        val result = TruckStopSearch.search(
            stops,
            TruckStopFilter(query = "Alpha", requiredAmenities = setOf(TruckStopAmenity.SHOWERS)),
        )
        assertEquals(listOf("a"), result.matches.map { it.id.value })
        assertEquals(0, result.hiddenUnknownCount)
    }
}
