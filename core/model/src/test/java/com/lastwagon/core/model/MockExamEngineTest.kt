package com.lastwagon.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class MockExamEngineTest {
    private fun q(id: String, correct: String) = PracticeQuestion(
        ContentId(id), ContentId("cat"), "prompt $id",
        listOf(AnswerChoice(ContentId("$id-a"), "a"), AnswerChoice(ContentId("$id-b"), "b")),
        ContentId(correct), "why",
    )
    private val pool = (1..10).map { q("q$it", "q$it-a") }

    @Test fun buildExamTakesRequestedSizeWithoutDuplicates() {
        val exam = MockExamEngine.buildExam(pool, 5, Random(1))
        assertEquals(5, exam.size)
        assertEquals(5, exam.map { it.id }.toSet().size)
    }

    @Test fun buildExamCapsAtPoolSizeAndHandlesEdgeCounts() {
        assertEquals(10, MockExamEngine.buildExam(pool, 50, Random(1)).size)
        assertTrue(MockExamEngine.buildExam(pool, 0, Random(1)).isEmpty())
        assertTrue(MockExamEngine.buildExam(emptyList(), 5, Random(1)).isEmpty())
    }

    @Test fun buildExamIsDeterministicForAGivenSeed() {
        assertEquals(
            MockExamEngine.buildExam(pool, 5, Random(42)).map { it.id },
            MockExamEngine.buildExam(pool, 5, Random(42)).map { it.id },
        )
    }

    @Test fun scoreCountsCorrectAnswersOnly() {
        val exam = pool.take(4)
        val answers = mapOf(
            ContentId("q1") to ContentId("q1-a"), // correct
            ContentId("q2") to ContentId("q2-b"), // wrong
            ContentId("q3") to ContentId("q3-a"), // correct
            // q4 unanswered
        )
        val score = MockExamEngine.score(exam, answers)
        assertEquals(2, score.correct)
        assertEquals(4, score.total)
        assertEquals(50, score.percent)
    }

    @Test fun missedIncludesWrongAndUnanswered() {
        val exam = pool.take(3)
        val answers = mapOf(ContentId("q1") to ContentId("q1-a")) // only q1 correct
        val missed = MockExamEngine.missed(exam, answers).map { it.id.value }
        assertEquals(listOf("q2", "q3"), missed)
    }

    @Test fun percentIsZeroForEmptyExam() {
        assertEquals(0, MockExamEngine.score(emptyList(), emptyMap()).percent)
    }
}
