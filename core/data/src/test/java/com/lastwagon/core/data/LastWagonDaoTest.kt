package com.lastwagon.core.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lastwagon.core.model.ApplicabilityFlag
import com.lastwagon.core.model.DailySafety
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** Exercises the actual Room v3 stack (entities, DAO, queries) end-to-end via Robolectric. */
@RunWith(AndroidJUnit4::class)
class LastWagonDaoTest {
    private lateinit var db: LastWagonDatabase
    private lateinit var dao: LastWagonDao

    @Before fun setUp() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, LastWagonDatabase::class.java)
            .allowMainThreadQueries().build()
        dao = db.dao()
    }

    @After fun tearDown() = db.close()

    @Test fun inspectionItemProvenanceRoundTripsThroughRoom() = runTest {
        dao.insertCategories(listOf(InspectionCategoryEntity("c", "Cab", 1)))
        dao.insertItems(listOf(
            InspectionItemEntity("i", "c", "Seat belt", "look", "damage", 1, true,
                "49 CFR 393.9", "VERIFIED",
                encodeApplicabilityFlags(setOf(ApplicabilityFlag.COMBO, ApplicabilityFlag.AIR))),
        ))
        val item = dao.observeInspectionItems().first().single()
        assertEquals("49 CFR 393.9", item.sourceCitation)
        assertEquals("VERIFIED", item.verificationStatus)
        assertEquals(
            setOf(ApplicabilityFlag.COMBO, ApplicabilityFlag.AIR),
            decodeApplicabilityFlags(item.applicabilityFlags),
        )
    }

    @Test fun dailyDayCompletionsPersistOnePerDayAndFeedStreak() = runTest {
        dao.insertDailyDayCompletion(DailyDayCompletionEntity(10, "q", true, 100))
        dao.insertDailyDayCompletion(DailyDayCompletionEntity(11, "q", false, 200))
        // Same day again replaces, staying one-per-day.
        dao.insertDailyDayCompletion(DailyDayCompletionEntity(10, "q2", true, 300))
        val days = dao.observeCompletedDailyDays().first().toSet()
        assertEquals(setOf(10L, 11L), days)
        assertEquals(2, DailySafety.currentStreak(days, 11))
    }

    @Test fun resetProgressClearsDayCompletions() = runTest {
        dao.insertDailyDayCompletion(DailyDayCompletionEntity(10, "q", true, 100))
        dao.resetProgress()
        assertEquals(emptySet<Long>(), dao.observeCompletedDailyDays().first().toSet())
    }

    @Test fun examResultsPersistNewestFirstAndResetClearsThem() = runTest {
        dao.insertExamResult(ExamResultEntity(categoryTitle = "General", correct = 3, total = 5, completedAtEpochMillis = 100))
        dao.insertExamResult(ExamResultEntity(categoryTitle = "Air Brakes", correct = 4, total = 5, completedAtEpochMillis = 200))
        val history = dao.observeExamResults().first()
        assertEquals(listOf("Air Brakes", "General"), history.map { it.categoryTitle })
        assertEquals(4, history.first().correct)
        dao.resetProgress()
        assertEquals(emptyList<ExamResultEntity>(), dao.observeExamResults().first())
    }
}
