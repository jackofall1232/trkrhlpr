package com.trkrhlpr.core.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.withTransaction
import com.trkrhlpr.core.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

private const val SAMPLE_VERSION = 1
private val Context.preferencesDataStore by preferencesDataStore("preferences")

class OfflineContentRepository(private val database: TrkrHlprDatabase) : ContentRepository {
    private val dao = database.dao()
    override suspend fun ensureSampleContent() {
        if (dao.contentVersionCount(SAMPLE_VERSION) > 0) return
        database.withTransaction {
            dao.insertCategories(SampleContent.inspectionCategories)
            dao.insertItems(SampleContent.inspectionItems)
            dao.insertTestCategories(SampleContent.testCategories)
            dao.insertQuestions(SampleContent.questions)
            dao.insertContentVersion(ContentVersionEntity(SAMPLE_VERSION, System.currentTimeMillis()))
        }
    }
    override fun observeInspectionCategories() =
        combine(dao.observeInspectionCategories(), dao.observeInspectionItems()) { categories, items ->
            categories.map { c -> InspectionCategory(ContentId(c.id), c.title, c.sequence, items.count { it.categoryId == c.id }) }
        }
    override fun observeInspectionItems() =
        dao.observeInspectionItems().map { values -> values.map { it.toModel() } }
    override fun observeTestCategories() =
        dao.observeTestCategories().map { values ->
            values.map { TestCategory(ContentId(it.id), it.title, TestKind.valueOf(it.kind), 1) }
        }
    override suspend fun getPracticeQuestion(categoryId: ContentId) =
        dao.getPracticeQuestion(categoryId.value)?.toPracticeQuestion()
    override suspend fun getDailyQuestion() = dao.getDailyQuestion()?.toDailyQuestion()
}

class OfflineProgressRepository(private val dao: TrkrHlprDao) : ProgressRepository {
    override fun observeInspectionProgress(): Flow<InspectionProgress> =
        combine(dao.observeCompletedInspectionIds(), dao.observeInspectionItems()) { completed, items ->
            InspectionProgress(completed.map(::ContentId).toSet(), items.size)
        }
    override fun observeProgressSnapshot(): Flow<ProgressSnapshot> =
        combine(observeInspectionProgress(), dao.observeTestAttemptCount(),
            dao.observeCorrectTestAttemptCount(), dao.observeDailyCompletionCount()
        ) { inspection, attempts, correct, daily ->
            ProgressSnapshot(inspection.completedCount, inspection.totalItems, attempts,
                if (attempts == 0) 0 else correct * 100 / attempts, daily > 0, if (daily > 0) 1 else 0)
        }
    override suspend fun setInspectionItemComplete(itemId: ContentId, complete: Boolean) {
        if (complete) dao.setInspectionCompletion(InspectionCompletionEntity(itemId.value, System.currentTimeMillis()))
        else dao.deleteInspectionCompletion(itemId.value)
    }
    override suspend fun recordPracticeAnswer(questionId: ContentId, result: AnswerResult) {
        dao.insertTestAttempt(TestAttemptEntity(questionId = questionId.value,
            selectedAnswerId = result.selectedAnswerId.value, correct = result.isCorrect,
            answeredAtEpochMillis = System.currentTimeMillis()))
    }
    override suspend fun completeDailyQuestion(questionId: ContentId, correct: Boolean) {
        dao.insertDailyCompletion(DailyCompletionEntity(questionId.value, correct, System.currentTimeMillis()))
    }
    override suspend fun resetAllProgress() = dao.resetProgress()
}

class DataStorePreferencesRepository(private val context: Context) : PreferencesRepository {
    private object Keys {
        val theme = stringPreferencesKey("theme")
        val reduceMotion = booleanPreferencesKey("reduce_motion")
        val largeText = booleanPreferencesKey("large_text")
    }
    override val preferences = context.preferencesDataStore.data.map { p ->
        UserPreferences(p[Keys.theme]?.let(ThemePreference::valueOf) ?: ThemePreference.DARK,
            p[Keys.reduceMotion] ?: false, p[Keys.largeText] ?: false)
    }
    override suspend fun setTheme(theme: ThemePreference) { context.preferencesDataStore.edit { it[Keys.theme] = theme.name } }
    override suspend fun setReduceMotion(enabled: Boolean) { context.preferencesDataStore.edit { it[Keys.reduceMotion] = enabled } }
    override suspend fun setLargeText(enabled: Boolean) { context.preferencesDataStore.edit { it[Keys.largeText] = enabled } }
}

private fun InspectionItemEntity.toModel() = InspectionItem(
    ContentId(id), ContentId(categoryId), title, inspectFor, sampleDefects, sequence, isSample)
private fun QuestionEntity.answers() = answersEncoded.split("|").mapIndexed { i, text ->
    AnswerChoice(ContentId("$id-answer-$i"), text)
}
private fun QuestionEntity.toPracticeQuestion() = PracticeQuestion(
    ContentId(id), ContentId(categoryId), prompt, answers(), ContentId(correctAnswerId),
    explanation, isSample)
private fun QuestionEntity.toDailyQuestion() = DailyQuestion(
    ContentId(id), prompt, answers(), ContentId(correctAnswerId), explanation, isSample)
