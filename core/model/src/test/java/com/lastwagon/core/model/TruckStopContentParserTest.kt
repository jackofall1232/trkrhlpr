package com.lastwagon.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TruckStopContentParserTest {

    private fun document(stops: String) = """
        {
          "schema_version": 1,
          "dataset": {"citation": "cite", "vintage": "vintage", "verification": "PARTIAL", "sample": false},
          "stops": [$stops]
        }
    """.trimIndent()

    private val validStop = """
        {"id": "s1", "name": "Stop One", "state": "OH", "highway": "I-75",
         "lat": 40.1, "lon": -84.2, "parking_spaces": 12, "diesel": true, "showers": false}
    """.trimIndent()

    @Test fun parsesAValidRecordWithDatasetProvenanceApplied() {
        val dataset = TruckStopContent.parse(document(validStop))
        assertEquals(1, dataset.stops.size)
        assertEquals(0, dataset.skippedRecords)
        val stop = dataset.stops.single()
        assertEquals("s1", stop.id.value)
        assertEquals("Stop One", stop.name)
        assertEquals("OH", stop.state)
        assertEquals(12, stop.truckParkingSpaces)
        assertEquals(true, stop.hasDiesel)
        assertEquals(false, stop.hasShowers)
        assertEquals("cite", stop.sourceCitation)
        assertEquals("vintage", stop.datasetVintage)
        assertEquals(VerificationStatus.PARTIAL, stop.verificationStatus)
        assertEquals(false, stop.isSample)
    }

    @Test fun missingAmenityKeysStayUnknownNeverFalse() {
        val dataset = TruckStopContent.parse(
            document("""{"id": "s1", "name": "N", "state": "IA", "lat": 41.0, "lon": -93.0}"""),
        )
        val stop = dataset.stops.single()
        assertNull(stop.hasFood)
        assertNull(stop.hasRepair)
        assertNull(stop.truckParkingSpaces)
        assertEquals("", stop.highway)
    }

    @Test fun structurallyBrokenRecordsAreSkippedAndCountedNotFatal() {
        val dataset = TruckStopContent.parse(
            document(
                """
                $validStop,
                {"name": "No id", "state": "OH", "lat": 40.0, "lon": -84.0},
                {"id": "s3", "name": "Bad coords", "state": "OH", "lat": 999.0, "lon": -84.0},
                {"id": "s4", "state": "OH", "lat": 40.0, "lon": -84.0},
                "not-an-object",
                {"id": "s6", "name": "Good Two", "state": "TX", "lat": 30.0, "lon": -95.0}
                """.trimIndent(),
            ),
        )
        assertEquals(listOf("s1", "s6"), dataset.stops.map { it.id.value })
        assertEquals(4, dataset.skippedRecords)
    }

    @Test fun duplicateIdsAreSkippedAndCountedNotSilentlyCollapsed() {
        // Room's REPLACE insert would silently keep only the last duplicate; the parser
        // must surface the defect instead so the installer can reject the document.
        val dataset = TruckStopContent.parse(
            document(
                """
                $validStop,
                {"id": "s1", "name": "Same Id Different Stop", "state": "TX", "lat": 30.0, "lon": -95.0}
                """.trimIndent(),
            ),
        )
        assertEquals(listOf("s1"), dataset.stops.map { it.id.value })
        assertEquals("Stop One", dataset.stops.single().name)
        assertEquals(1, dataset.skippedRecords)
    }

    @Test fun negativeParkingCountIsTreatedAsUnknown() {
        val dataset = TruckStopContent.parse(
            document("""{"id": "s1", "name": "N", "state": "IA", "lat": 41.0, "lon": -93.0, "parking_spaces": -5}"""),
        )
        assertNull(dataset.stops.single().truckParkingSpaces)
    }

    @Test fun unknownVerificationStatusDefaultsToUnverified() {
        val json = """
            {"schema_version": 1,
             "dataset": {"citation": "c", "vintage": "v", "verification": "not-a-status"},
             "stops": [{"id": "s1", "name": "N", "state": "IA", "lat": 41.0, "lon": -93.0}]}
        """.trimIndent()
        val stop = TruckStopContent.parse(json).stops.single()
        assertEquals(VerificationStatus.UNVERIFIED, stop.verificationStatus)
        // With no explicit sample flag, content defaults to sample — never silently real.
        assertTrue(stop.isSample)
    }

    @Test fun wrongSchemaVersionYieldsEmptyDataset() {
        val json = """{"schema_version": 2, "dataset": {}, "stops": [$validStop]}"""
        val dataset = TruckStopContent.parse(json)
        assertTrue(dataset.stops.isEmpty())
        assertEquals(0, dataset.skippedRecords)
    }

    @Test fun garbageInputYieldsEmptyDatasetWithoutThrowing() {
        assertTrue(TruckStopContent.parse("not json at all").stops.isEmpty())
        assertTrue(TruckStopContent.parse("[]").stops.isEmpty())
        assertTrue(TruckStopContent.parse("{}").stops.isEmpty())
    }
}
