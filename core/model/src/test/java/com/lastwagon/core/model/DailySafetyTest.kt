package com.lastwagon.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DailySafetyTest {
    private fun q(id: String) = DailyQuestion(ContentId(id), "prompt $id", emptyList(), ContentId("$id-a"), "why")
    private val pool = listOf(q("a"), q("b"), q("c"))

    @Test fun selectionIsDeterministicPerDayAndCyclesThePool() {
        assertEquals("a", DailySafety.selectForDay(pool, 0)!!.id.value)
        assertEquals("b", DailySafety.selectForDay(pool, 1)!!.id.value)
        assertEquals("c", DailySafety.selectForDay(pool, 2)!!.id.value)
        assertEquals("a", DailySafety.selectForDay(pool, 3)!!.id.value)
        // Same day always yields the same question.
        assertEquals(DailySafety.selectForDay(pool, 100)!!.id, DailySafety.selectForDay(pool, 100)!!.id)
    }

    @Test fun selectionIndependentOfPoolOrder() {
        assertEquals(
            DailySafety.selectForDay(pool, 5)!!.id,
            DailySafety.selectForDay(pool.reversed(), 5)!!.id,
        )
    }

    @Test fun selectionHandlesEmptyAndNegativeDays() {
        assertNull(DailySafety.selectForDay(emptyList(), 3))
        assertEquals("a", DailySafety.selectForDay(pool, -3)!!.id.value)
        assertEquals("c", DailySafety.selectForDay(pool, -1)!!.id.value)
    }

    @Test fun streakZeroWhenNothingCompleted() {
        assertEquals(0, DailySafety.currentStreak(emptySet(), 100))
    }

    @Test fun streakCountsConsecutiveDaysIncludingToday() {
        assertEquals(3, DailySafety.currentStreak(setOf(98L, 99L, 100L), 100))
    }

    @Test fun streakHoldsFromYesterdayWhenTodayNotYetDone() {
        // Today (101) not completed yet, but 99+100 are → streak still 2, not broken.
        assertEquals(2, DailySafety.currentStreak(setOf(99L, 100L), 101))
    }

    @Test fun streakBreaksOnAMissedDay() {
        // Gap at day 100 → only today counts once 101 is present.
        assertEquals(1, DailySafety.currentStreak(setOf(98L, 99L, 101L), 101))
        // Missed both yesterday and today → streak 0.
        assertEquals(0, DailySafety.currentStreak(setOf(97L, 98L), 101))
    }
}
