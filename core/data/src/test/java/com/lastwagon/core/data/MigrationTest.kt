package com.lastwagon.core.data

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Executes the real ALTER/CREATE migrations against on-disk SQLite via Robolectric, validating
 * the resulting schema against the latest exported schema JSON. Complements the pure
 * encode/decode tests in [InspectionContentSchemaTest] and [TruckStopContentTest].
 */
@RunWith(AndroidJUnit4::class)
class MigrationTest {
    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        LastWagonDatabase::class.java,
    )

    @Test
    fun migratesFromV1ToV5PreservingRowsAndDefaults() {
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL("INSERT INTO inspection_categories (id,title,sequence) VALUES ('c','Cab',1)")
            execSQL(
                "INSERT INTO inspection_items (id,categoryId,title,inspectFor,sampleDefects,sequence,isSample) " +
                    "VALUES ('i','c','Seat belt','look','damage',1,1)",
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(
            TEST_DB, 5, true, MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5,
        )

        // v2 columns exist on the pre-existing row with their NOT NULL defaults.
        db.query("SELECT sourceCitation, verificationStatus, applicabilityFlags FROM inspection_items WHERE id='i'")
            .use { c ->
                assertTrue(c.moveToFirst())
                assertEquals("", c.getString(0))
                assertEquals("UNVERIFIED", c.getString(1))
                assertEquals("", c.getString(2))
            }
        // v3 + v4 tables exist and accept rows.
        db.execSQL(
            "INSERT INTO daily_day_completions (epochDay,questionId,correct,completedAtEpochMillis) " +
                "VALUES (1,'q',1,10)",
        )
        db.execSQL(
            "INSERT INTO exam_results (categoryTitle,correct,total,completedAtEpochMillis) " +
                "VALUES ('General',3,5,10)",
        )
        db.query("SELECT COUNT(*) FROM exam_results").use { c ->
            assertTrue(c.moveToFirst()); assertEquals(1, c.getInt(0))
        }
        // v5 truck_stops exists: provenance columns take their NOT NULL defaults and the
        // amenity/parking columns accept NULL (unknown, never coerced to false/zero).
        db.execSQL(
            "INSERT INTO truck_stops (id,name,state,highway,latitude,longitude,isSample) " +
                "VALUES ('t','Stop','OH','I-75',40.1,-84.2,1)",
        )
        db.query(
            "SELECT sourceCitation, verificationStatus, datasetVintage, hasDiesel " +
                "FROM truck_stops WHERE id='t'",
        ).use { c ->
            assertTrue(c.moveToFirst())
            assertEquals("", c.getString(0))
            assertEquals("UNVERIFIED", c.getString(1))
            assertEquals("", c.getString(2))
            assertTrue(c.isNull(3))
        }
        db.close()
    }

    private companion object { const val TEST_DB = "migration-test-db" }
}
