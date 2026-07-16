package com.lastwagon.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InspectionProgressTest {
    @Test fun fractionAndCompletionReflectCompletedItems() {
        val progress = InspectionProgress(setOf(ContentId("one"), ContentId("two")), 4)
        assertEquals(0.5f, progress.fraction)
        assertEquals(2, progress.completedCount)
        assertFalse(progress.isComplete)
        assertTrue(progress.copy(totalItems = 2).isComplete)
    }
}
