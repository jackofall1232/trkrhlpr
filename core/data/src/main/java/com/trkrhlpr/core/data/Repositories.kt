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
        combine(observeInspectionProgress(), dao.observeTestAttemptStats(), dao.observeDailyCompletionCount()
        ) { inspection, stats, daily ->
            ProgressSnapshot(inspection.completedCount, inspection.totalItems, stats.total,
                if (stats.total == 0) 0 else stats.correct * 100 / stats.total,
                daily > 0, if (daily > 0) 1 else 0)
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
        UserPreferences(parseThemePreference(p[Keys.theme]),
            p[Keys.reduceMotion] ?: false, p[Keys.largeText] ?: false)
    }
    override suspend fun setTheme(theme: ThemePreference) { context.preferencesDataStore.edit { it[Keys.theme] = theme.name } }
    override suspend fun setReduceMotion(enabled: Boolean) { context.preferencesDataStore.edit { it[Keys.reduceMotion] = enabled } }
    override suspend fun setLargeText(enabled: Boolean) { context.preferencesDataStore.edit { it[Keys.largeText] = enabled } }
}

class DataStoreVehicleProfileRepository(private val context: Context) : VehicleProfileRepository {
    private object Keys {
        val schemaVersion = intPreferencesKey("vehicle_profile_schema_version")
        val vehicleType = stringPreferencesKey("vehicle_profile_type")
        val heightMeters = stringPreferencesKey("vehicle_profile_height_meters")
        val widthMeters = stringPreferencesKey("vehicle_profile_width_meters")
        val lengthMeters = stringPreferencesKey("vehicle_profile_length_meters")
        val grossWeightTonnes = stringPreferencesKey("vehicle_profile_gross_weight_tonnes")
        val axleLoadTonnes = stringPreferencesKey("vehicle_profile_axle_load_tonnes")
        val axleCount = intPreferencesKey("vehicle_profile_axle_count")
        val hazmat = booleanPreferencesKey("vehicle_profile_hazmat")
        val avoidTolls = booleanPreferencesKey("vehicle_profile_avoid_tolls")
        val avoidFerries = booleanPreferencesKey("vehicle_profile_avoid_ferries")
        val avoidUnpaved = booleanPreferencesKey("vehicle_profile_avoid_unpaved")
        val confirmedAt = longPreferencesKey("vehicle_profile_confirmed_at")
    }

    override val profile = context.preferencesDataStore.data.map(::decodeVehicleProfile)

    override suspend fun save(profile: VehicleProfile) {
        require(VehicleProfileValidator.validate(profile).isEmpty()) { "Vehicle profile is invalid" }
        context.preferencesDataStore.edit { p ->
            p[Keys.schemaVersion] = VEHICLE_PROFILE_SCHEMA_VERSION
            p[Keys.vehicleType] = profile.vehicleType.name
            p[Keys.heightMeters] = profile.heightMeters.toString()
            p[Keys.widthMeters] = profile.widthMeters.toString()
            p[Keys.lengthMeters] = profile.lengthMeters.toString()
            p[Keys.grossWeightTonnes] = profile.grossWeightTonnes.toString()
            p[Keys.axleLoadTonnes] = profile.axleLoadTonnes.toString()
            p[Keys.axleCount] = profile.axleCount
            p[Keys.hazmat] = profile.hazmat
            p[Keys.avoidTolls] = profile.avoidTolls
            p[Keys.avoidFerries] = profile.avoidFerries
            p[Keys.avoidUnpaved] = profile.avoidUnpavedRoads
            p[Keys.confirmedAt] = profile.confirmedAtEpochMillis
        }
    }

    override suspend fun clear() {
        context.preferencesDataStore.edit { p ->
            p.remove(Keys.schemaVersion)
            p.remove(Keys.vehicleType)
            p.remove(Keys.heightMeters)
            p.remove(Keys.widthMeters)
            p.remove(Keys.lengthMeters)
            p.remove(Keys.grossWeightTonnes)
            p.remove(Keys.axleLoadTonnes)
            p.remove(Keys.axleCount)
            p.remove(Keys.hazmat)
            p.remove(Keys.avoidTolls)
            p.remove(Keys.avoidFerries)
            p.remove(Keys.avoidUnpaved)
            p.remove(Keys.confirmedAt)
        }
    }

    private fun decodeVehicleProfile(p: Preferences): VehicleProfile? {
        return decodeStoredVehicleProfile(
            p[Keys.schemaVersion], p[Keys.vehicleType], p[Keys.heightMeters], p[Keys.widthMeters],
            p[Keys.lengthMeters], p[Keys.grossWeightTonnes], p[Keys.axleLoadTonnes],
            p[Keys.axleCount], p[Keys.hazmat], p[Keys.avoidTolls], p[Keys.avoidFerries],
            p[Keys.avoidUnpaved], p[Keys.confirmedAt],
        )
    }

    private companion object { const val VEHICLE_PROFILE_SCHEMA_VERSION = 1 }
}

internal fun decodeStoredVehicleProfile(
    schemaVersion: Int?, vehicleType: String?, heightMeters: String?, widthMeters: String?,
    lengthMeters: String?, grossWeightTonnes: String?, axleLoadTonnes: String?,
    axleCount: Int?, hazmat: Boolean?, avoidTolls: Boolean?, avoidFerries: Boolean?,
    avoidUnpaved: Boolean?, confirmedAt: Long?,
): VehicleProfile? {
    if (schemaVersion != 1) return null
    return runCatching {
        VehicleProfile(
            CommercialVehicleType.valueOf(requireNotNull(vehicleType)),
            requireNotNull(heightMeters).toDouble(), requireNotNull(widthMeters).toDouble(),
            requireNotNull(lengthMeters).toDouble(), requireNotNull(grossWeightTonnes).toDouble(),
            requireNotNull(axleLoadTonnes).toDouble(), requireNotNull(axleCount), hazmat ?: false,
            avoidTolls ?: false, avoidFerries ?: false, avoidUnpaved ?: false,
            requireNotNull(confirmedAt),
        )
    }.getOrNull()?.takeIf { VehicleProfileValidator.validate(it).isEmpty() }
}

internal fun parseThemePreference(value: String?): ThemePreference =
    ThemePreference.entries.firstOrNull { it.name == value } ?: ThemePreference.DARK

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
