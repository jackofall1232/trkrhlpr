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

enum class CommercialVehicleType {
    TRACTOR_TRAILER, STRAIGHT_TRUCK, BUS, DELIVERY, OTHER,
}

data class VehicleProfile(
    val vehicleType: CommercialVehicleType,
    val heightMeters: Double,
    val widthMeters: Double,
    val lengthMeters: Double,
    val grossWeightTonnes: Double,
    val axleLoadTonnes: Double,
    val axleCount: Int,
    val hazmat: Boolean,
    val avoidTolls: Boolean,
    val avoidFerries: Boolean,
    val avoidUnpavedRoads: Boolean,
    val confirmedAtEpochMillis: Long,
)

enum class VehicleProfileField { HEIGHT, WIDTH, LENGTH, GROSS_WEIGHT, AXLE_LOAD, AXLE_COUNT, CONFIRMATION }
data class VehicleProfileValidationError(val field: VehicleProfileField, val message: String)

object VehicleProfileValidator {
    fun validate(profile: VehicleProfile): List<VehicleProfileValidationError> = buildList {
        checkRange(profile.heightMeters, 1.0, 6.0, VehicleProfileField.HEIGHT, "Height must be between 1 and 6 m.")
        checkRange(profile.widthMeters, 1.0, 4.0, VehicleProfileField.WIDTH, "Width must be between 1 and 4 m.")
        checkRange(profile.lengthMeters, 2.0, 40.0, VehicleProfileField.LENGTH, "Length must be between 2 and 40 m.")
        checkRange(profile.grossWeightTonnes, 1.0, 100.0, VehicleProfileField.GROSS_WEIGHT,
            "Gross weight must be between 1 and 100 metric tonnes.")
        checkRange(profile.axleLoadTonnes, 0.5, 30.0, VehicleProfileField.AXLE_LOAD,
            "Axle load must be between 0.5 and 30 metric tonnes.")
        if (profile.axleLoadTonnes > profile.grossWeightTonnes) add(VehicleProfileValidationError(
            VehicleProfileField.AXLE_LOAD, "Axle load cannot exceed gross weight."))
        if (profile.axleCount !in 2..20) add(VehicleProfileValidationError(
            VehicleProfileField.AXLE_COUNT, "Axle count must be between 2 and 20."))
        if (profile.confirmedAtEpochMillis <= 0) add(VehicleProfileValidationError(
            VehicleProfileField.CONFIRMATION, "Confirm the current vehicle and load before saving."))
    }

    private fun MutableList<VehicleProfileValidationError>.checkRange(
        value: Double, minimum: Double, maximum: Double,
        field: VehicleProfileField, message: String,
    ) {
        if (!value.isFinite() || value !in minimum..maximum) add(VehicleProfileValidationError(field, message))
    }
}

object VehicleUnitConversions {
    private const val METERS_PER_FOOT = 0.3048
    private const val POUNDS_PER_METRIC_TONNE = 2204.62262185
    fun feetToMeters(feet: Double) = feet * METERS_PER_FOOT
    fun metersToFeet(meters: Double) = meters / METERS_PER_FOOT
    fun poundsToMetricTonnes(pounds: Double) = pounds / POUNDS_PER_METRIC_TONNE
    fun metricTonnesToPounds(tonnes: Double) = tonnes * POUNDS_PER_METRIC_TONNE
}

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
interface VehicleProfileRepository {
    val profile: Flow<VehicleProfile?>
    suspend fun save(profile: VehicleProfile)
    suspend fun clear()
}

data class GeoPoint(val latitude: Double, val longitude: Double) {
    val isValid get() = latitude in -90.0..90.0 && longitude in -180.0..180.0 &&
        latitude.isFinite() && longitude.isFinite()
}

data class RouteRequest(
    val origin: GeoPoint,
    val destination: GeoPoint,
    val vehicleProfile: VehicleProfile,
)

data class RouteStep(
    val instruction: String,
    val distanceMeters: Double,
    val durationSeconds: Double,
)

data class RouteProvenance(
    val requestId: String,
    val providerId: String,
    val endpoint: String,
    val routingProfile: String,
    val requestedAtEpochMillis: Long,
    val receivedAtEpochMillis: Long,
    val requestPayload: String,
    val responseSha256: String,
    val responseAttribution: String?,
    val responseService: String?,
    val responseTimestampEpochMillis: Long?,
    val engineVersion: String?,
    val engineBuildDate: String?,
    val graphDate: String?,
)

data class CalculatedRoute(
    val request: RouteRequest,
    val geometry: List<GeoPoint>,
    val distanceMeters: Double,
    val durationSeconds: Double,
    val steps: List<RouteStep>,
    val warnings: List<String>,
    val roadAccessRestrictionSegments: Int,
    val provenance: RouteProvenance,
)

enum class RouteFailureKind {
    MISSING_CREDENTIAL, INVALID_REQUEST, NETWORK, TIMEOUT, RATE_LIMITED,
    PROVIDER_REJECTED, NO_ROUTE, MALFORMED_RESPONSE, STORAGE,
}

data class RouteFailure(
    val kind: RouteFailureKind,
    val message: String,
    val httpStatus: Int? = null,
    val retryable: Boolean = false,
)

sealed interface RouteCalculationResult {
    data class Success(val route: CalculatedRoute) : RouteCalculationResult
    data class Failure(val error: RouteFailure) : RouteCalculationResult
}

interface RoutingProvider {
    val id: String
    suspend fun calculate(request: RouteRequest): RouteCalculationResult
}

interface RouteRepository {
    val lastRoute: Flow<CalculatedRoute?>
    suspend fun save(route: CalculatedRoute)
    suspend fun clear()
}
