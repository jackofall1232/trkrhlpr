package com.lastwagon.core.data

import com.lastwagon.core.model.VerificationStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pure-JVM invariants for the labeled sample truck-stop rows (Track C phase 1) and the
 *  4 -> 5 migration metadata. The executed-migration validation lives in [MigrationTest]. */
class TruckStopContentTest {

    @Test fun sampleTruckStopsAreLabeledFictionalAndCited() {
        assertTrue(SampleContent.truckStops.isNotEmpty())
        SampleContent.truckStops.forEach { stop ->
            assertTrue(stop.id.startsWith("sample-"))
            assertTrue(stop.isSample)
            assertTrue(stop.name.contains("Sample"))
            assertTrue(stop.sourceCitation.isNotBlank())
            assertTrue(stop.datasetVintage.isNotBlank())
            assertEquals(VerificationStatus.UNVERIFIED, parseVerificationStatus(stop.verificationStatus))
        }
    }

    @Test fun sampleTruckStopIdsAreUnique() {
        assertEquals(
            SampleContent.truckStops.size,
            SampleContent.truckStops.map { it.id }.distinct().size,
        )
    }

    @Test fun sampleTruckStopCoordinatesAndStatesArePlausible() {
        SampleContent.truckStops.forEach { stop ->
            assertTrue(stop.latitude in -90.0..90.0)
            assertTrue(stop.longitude in -180.0..180.0)
            assertTrue(stop.state.matches(Regex("[A-Z]{2}")))
            assertTrue(stop.highway.isNotBlank())
        }
    }

    @Test fun samplePoolExercisesTheThreeStateAmenityModel() {
        // The pool must keep at least one recorded-true, one recorded-false, and one unknown
        // amenity so the unknown-vs-absent display and filter behavior stay exercised.
        assertTrue(SampleContent.truckStops.any { it.hasDiesel == true })
        assertTrue(SampleContent.truckStops.any { it.hasRepair == false })
        assertTrue(SampleContent.truckStops.any { it.hasDiesel == null })
        assertTrue(SampleContent.truckStops.any { it.truckParkingSpaces == null })
    }

    @Test fun migration4To5HasCorrectVersionBounds() {
        assertEquals(4, MIGRATION_4_5.startVersion)
        assertEquals(5, MIGRATION_4_5.endVersion)
    }
}
