package com.trkrhlpr.core.data

import org.junit.Assert.assertEquals
import org.junit.Test

class AnswerEncodingTest {
    @Test fun sampleContentHasOneQuestionForEveryCategory() {
        assertEquals(
            SampleContent.testCategories.size,
            SampleContent.questions.count { it.type == "practice" },
        )
    }
}
