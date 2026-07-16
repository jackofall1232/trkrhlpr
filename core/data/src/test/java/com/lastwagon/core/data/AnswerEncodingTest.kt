package com.lastwagon.core.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnswerEncodingTest {
    @Test fun everyCategoryHasAPracticeQuestionPool() {
        val byCategory = SampleContent.questions
            .filter { it.type == "practice" }
            .groupBy { it.categoryId }
        // One pool per category, each with enough questions to randomize a mock exam.
        assertEquals(SampleContent.testCategories.size, byCategory.size)
        byCategory.values.forEach { assertTrue("pool has >= 2 questions", it.size >= 2) }
    }
}
