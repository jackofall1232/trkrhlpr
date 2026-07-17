package com.lastwagon.core.data

import com.lastwagon.core.model.TruckStopContent
import com.lastwagon.core.model.VerificationStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pure-JVM invariants for the labeled sample truck-stop content document (Track C) and
 *  the 4 -> 5 migration metadata. Parser edge cases live in core/model's
 *  TruckStopContentParserTest; the executed-migration validation lives in [MigrationTest]. */
class TruckStopContentTest {

    private val dataset = TruckStopContent.parse(SampleContent.truckStopsJson)

    @Test fun sampleDocumentParsesCompletely() {
        assertEquals(6, dataset.stops.size)
        assertEquals(0, dataset.skippedRecords)
    }

    @Test fun sampleTruckStopsAreLabeledFictionalAndCited() {
        dataset.stops.forEach { stop ->
            assertTrue(stop.id.value.startsWith("sample-"))
            assertTrue(stop.isSample)
            assertTrue(stop.name.contains("Sample"))
            assertTrue(stop.sourceCitation.isNotBlank())
            assertTrue(stop.datasetVintage.isNotBlank())
            assertEquals(VerificationStatus.UNVERIFIED, stop.verificationStatus)
        }
    }

    @Test fun sampleTruckStopIdsAreUnique() {
        assertEquals(dataset.stops.size, dataset.stops.map { it.id }.distinct().size)
    }

    @Test fun sampleTruckStopCoordinatesAndStatesArePlausible() {
        dataset.stops.forEach { stop ->
            assertTrue(stop.latitude in -90.0..90.0)
            assertTrue(stop.longitude in -180.0..180.0)
            assertTrue(stop.state.matches(Regex("[A-Z]{2}")))
            assertTrue(stop.highway.isNotBlank())
        }
    }

    @Test fun samplePoolExercisesTheThreeStateAmenityModel() {
        // The pool must keep at least one recorded-true, one recorded-false, and one unknown
        // amenity so the unknown-vs-absent display and filter behavior stay exercised.
        assertTrue(dataset.stops.any { it.hasDiesel == true })
        assertTrue(dataset.stops.any { it.hasRepair == false })
        assertTrue(dataset.stops.any { it.hasDiesel == null })
        assertTrue(dataset.stops.any { it.truckParkingSpaces == null })
    }

    @Test fun migration4To5HasCorrectVersionBounds() {
        assertEquals(4, MIGRATION_4_5.startVersion)
        assertEquals(5, MIGRATION_4_5.endVersion)
    }
}
