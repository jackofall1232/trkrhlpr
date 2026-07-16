package com.trkrhlpr.core.model

import kotlinx.coroutines.flow.Flow

@JvmInline value class ContentId(val value: String)

data class InspectionCategory(val id: ContentId, val title: String, val sequence: Int, val itemCount: Int)
data class InspectionItem(
    val id: ContentId, val categoryId: ContentId, val title: String, val inspectFor: String,
    val sampleDefects: String, val sequence: Int, val isSample: Boolean = true,
)
data class InspectionProgress(val completedItemIds: Set<ContentId> = emptySet(), val totalItems: Int = 0) {
    val completedCount get() = completedItemIds.size
    val fraction get() = if (totalItems == 0) 0f else completedCount.toFloat() / totalItems
    val isComplete get() = totalItems > 0 && completedCount == totalItems
}
enum class TestKind { CLASS_A, CLASS_B, GENERAL, AIR_BRAKES, COMBINATION, HAZMAT, TANKER, PASSENGER }
data class TestCategory(val id: ContentId, val title: String, val kind: TestKind, val availableQuestionCount: Int)
data class AnswerChoice(val id: ContentId, val text: String)
data class PracticeQuestion(
    val id: ContentId, val categoryId: ContentId, val prompt: String, val answers: List<AnswerChoice>,
    val correctAnswerId: ContentId, val explanation: String, val isSample: Boolean = true,
)
data class AnswerResult(val selectedAnswerId: ContentId, val isCorrect: Boolean, val explanation: String)
data class DailyQuestion(
    val id: ContentId, val prompt: String, val answers: List<AnswerChoice>,
    val correctAnswerId: ContentId, val explanation: String, val isSample: Boolean = true,
)
enum class ThemePreference { DARK, LIGHT, SYSTEM }
data class UserPreferences(
    val theme: ThemePreference = ThemePreference.DARK,
    val reduceMotion: Boolean = false,
    val largeText: Boolean = false,
)
data class ProgressSnapshot(
    val inspectionCompleted: Int = 0, val inspectionTotal: Int = 0,
    val testsCompleted: Int = 0, val testAccuracyPercent: Int = 0,
    val dailyCompleted: Boolean = false, val dailyStreak: Int = 0,
)

interface ContentRepository {
    suspend fun ensureSampleContent()
    fun observeInspectionCategories(): Flow<List<InspectionCategory>>
    fun observeInspectionItems(): Flow<List<InspectionItem>>
    fun observeTestCategories(): Flow<List<TestCategory>>
    suspend fun getPracticeQuestion(categoryId: ContentId): PracticeQuestion?
    suspend fun getDailyQuestion(): DailyQuestion?
}
interface ProgressRepository {
    fun observeInspectionProgress(): Flow<InspectionProgress>
    fun observeProgressSnapshot(): Flow<ProgressSnapshot>
    suspend fun setInspectionItemComplete(itemId: ContentId, complete: Boolean)
    suspend fun recordPracticeAnswer(questionId: ContentId, result: AnswerResult)
    suspend fun completeDailyQuestion(questionId: ContentId, correct: Boolean)
    suspend fun resetAllProgress()
}
interface PreferencesRepository {
    val preferences: Flow<UserPreferences>
    suspend fun setTheme(theme: ThemePreference)
    suspend fun setReduceMotion(enabled: Boolean)
    suspend fun setLargeText(enabled: Boolean)
}
