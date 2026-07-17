package com.lastwagon.core.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.withTransaction
import com.lastwagon.core.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

private const val SAMPLE_VERSION = 3
private val Context.preferencesDataStore by preferencesDataStore("preferences")

// Driver-supplied credentials get their own store so the app's backup rules can exclude
// exactly this file (datastore/local_secrets.preferences_pb) from cloud backup and device
// transfer without dropping ordinary preferences — see app/src/main/res/xml/.
private val Context.localSecretsDataStore by preferencesDataStore("local_secrets")

class OfflineContentRepository(private val database: LastWagonDatabase) : ContentRepository {
    private val dao = database.dao()
    override suspend fun ensureSampleContent() {
        if (dao.contentVersionCount(SAMPLE_VERSION) > 0) return
        val truckStops = TruckStopContent.parse(SampleContent.truckStopsJson)
        database.withTransaction {
            dao.insertCategories(SampleContent.inspectionCategories)
            dao.insertItems(SampleContent.inspectionItems)
            dao.insertTestCategories(SampleContent.testCategories)
            dao.insertQuestions(SampleContent.questions)
            // Replace, never merge: clearing first guarantees rows from a prior dataset
            // (different ids) cannot linger beside the new one. No-silent-truncation: a
            // document that dropped records is a content defect — keep the previous
            // directory rather than install a partial dataset (the bundled document is
            // test-guaranteed to parse completely in TruckStopContentTest).
            if (truckStops.skippedRecords == 0 && truckStops.stops.isNotEmpty()) {
                dao.clearTruckStops()
                dao.insertTruckStops(truckStops.stops.map { it.toEntity() })
            }
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
    override suspend fun getPracticeQuestions(categoryId: ContentId) =
        dao.getPracticeQuestions(categoryId.value).map { it.toPracticeQuestion() }
    override suspend fun getDailyQuestion() = dao.getDailyQuestion()?.toDailyQuestion()
    override suspend fun getDailyQuestions() = dao.getDailyQuestions().map { it.toDailyQuestion() }
    override fun observeTruckStops() =
        dao.observeTruckStops().map { values -> values.map { it.toModel() } }
}

/** UTC day index used for daily-question selection and streaks. */
internal const val EPOCH_DAY_MILLIS = 86_400_000L
internal fun epochDayOf(epochMillis: Long) = epochMillis / EPOCH_DAY_MILLIS

class OfflineProgressRepository(private val dao: LastWagonDao) : ProgressRepository {
    override fun observeInspectionProgress(): Flow<InspectionProgress> =
        combine(dao.observeCompletedInspectionIds(), dao.observeInspectionItems()) { completed, items ->
            InspectionProgress(completed.map(::ContentId).toSet(), items.size)
        }
    override fun observeCompletedDailyDays(): Flow<Set<Long>> =
        dao.observeCompletedDailyDays().map { it.toSet() }
    override suspend fun recordExamResult(categoryTitle: String, score: ExamScore) {
        dao.insertExamResult(ExamResultEntity(
            categoryTitle = categoryTitle, correct = score.correct, total = score.total,
            completedAtEpochMillis = System.currentTimeMillis()))
    }
    override fun observeExamHistory(): Flow<List<ExamResult>> =
        dao.observeExamResults().map { results -> results.map { it.toModel() } }
    override fun observeProgressSnapshot(): Flow<ProgressSnapshot> =
        combine(observeInspectionProgress(), dao.observeTestAttemptStats(), observeCompletedDailyDays()
        ) { inspection, stats, days ->
            val today = epochDayOf(System.currentTimeMillis())
            ProgressSnapshot(inspection.completedCount, inspection.totalItems, stats.total,
                if (stats.total == 0) 0 else stats.correct * 100 / stats.total,
                today in days, DailySafety.currentStreak(days, today))
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
        val now = System.currentTimeMillis()
        dao.insertDailyDayCompletion(DailyDayCompletionEntity(epochDayOf(now), questionId.value, correct, now))
    }
    override suspend fun resetAllProgress() = dao.resetProgress()
}

class DataStorePreferencesRepository(private val context: Context) : PreferencesRepository {
    private object Keys {
        val theme = stringPreferencesKey("theme")
        val reduceMotion = booleanPreferencesKey("reduce_motion")
        val largeText = booleanPreferencesKey("large_text")
        val orsApiKeyOverride = stringPreferencesKey("ors_api_key_override")
    }
    override val preferences = context.preferencesDataStore.data
        .combine(context.localSecretsDataStore.data) { p, secrets ->
            UserPreferences(parseThemePreference(p[Keys.theme]),
                p[Keys.reduceMotion] ?: false, p[Keys.largeText] ?: false,
                secrets[Keys.orsApiKeyOverride].orEmpty())
        }
    override suspend fun setTheme(theme: ThemePreference) { context.preferencesDataStore.edit { it[Keys.theme] = theme.name } }
    override suspend fun setReduceMotion(enabled: Boolean) { context.preferencesDataStore.edit { it[Keys.reduceMotion] = enabled } }
    override suspend fun setLargeText(enabled: Boolean) { context.preferencesDataStore.edit { it[Keys.largeText] = enabled } }
    override suspend fun setOrsApiKeyOverride(key: String) {
        context.localSecretsDataStore.edit { secrets ->
            val trimmed = key.trim()
            if (trimmed.isEmpty()) secrets.remove(Keys.orsApiKeyOverride)
            else secrets[Keys.orsApiKeyOverride] = trimmed
        }
    }
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
    ContentId(id), ContentId(categoryId), title, inspectFor, sampleDefects, sequence, isSample,
    sourceCitation, parseVerificationStatus(verificationStatus),
    decodeApplicabilityFlags(applicabilityFlags))

private const val APPLICABILITY_FLAG_DELIMITER = ","

/** Canonical column encoding for an item's applicability flags: sorted, delimiter-joined
 *  flag names (empty string for "always applies"). Kept sorted so the stored value is stable. */
internal fun encodeApplicabilityFlags(flags: Set<ApplicabilityFlag>): String =
    flags.map { it.name }.sorted().joinToString(APPLICABILITY_FLAG_DELIMITER)

internal fun decodeApplicabilityFlags(encoded: String): Set<ApplicabilityFlag> =
    encoded.split(APPLICABILITY_FLAG_DELIMITER)
        .mapNotNull { token ->
            token.trim().takeIf { it.isNotEmpty() }
                ?.let { name -> ApplicabilityFlag.entries.firstOrNull { it.name == name } }
        }
        .toSet()

internal fun parseVerificationStatus(value: String?): VerificationStatus =
    VerificationStatus.entries.firstOrNull { it.name == value } ?: VerificationStatus.UNVERIFIED
private fun QuestionEntity.answers() = answersEncoded.split("|").mapIndexed { i, text ->
    AnswerChoice(ContentId("$id-answer-$i"), text)
}
private fun QuestionEntity.toPracticeQuestion() = PracticeQuestion(
    ContentId(id), ContentId(categoryId), prompt, answers(), ContentId(correctAnswerId),
    explanation, isSample)
private fun QuestionEntity.toDailyQuestion() = DailyQuestion(
    ContentId(id), prompt, answers(), ContentId(correctAnswerId), explanation, isSample)
private fun ExamResultEntity.toModel() = ExamResult(
    id.toString(), categoryTitle, ExamScore(correct, total), completedAtEpochMillis)
private fun TruckStopEntity.toModel() = TruckStop(
    ContentId(id), name, state, highway, latitude, longitude, truckParkingSpaces,
    hasDiesel, hasShowers, hasFood, hasRepair, isSample,
    sourceCitation, parseVerificationStatus(verificationStatus), datasetVintage)
private fun TruckStop.toEntity() = TruckStopEntity(
    id.value, name, state, highway, latitude, longitude, truckParkingSpaces,
    hasDiesel, hasShowers, hasFood, hasRepair, isSample,
    sourceCitation, verificationStatus.name, datasetVintage)
