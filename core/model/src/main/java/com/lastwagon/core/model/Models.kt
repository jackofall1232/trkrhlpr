package com.lastwagon.core.model

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull

@JvmInline value class ContentId(val value: String)

data class InspectionCategory(val id: ContentId, val title: String, val sequence: Int, val itemCount: Int)

/** How well an inspection item's content is traceable to an authoritative source. Mirrors the
 *  C / P / U statuses tracked in docs/pretrip-132-checklist.md. */
enum class VerificationStatus { VERIFIED, PARTIAL, UNVERIFIED }

/** Applicability flags used to filter items by the driver's vehicle configuration.
 *  An item with no flags always applies. COMBO and AIR are hard include filters (an item so
 *  flagged applies only to combination / air-brake-equipped vehicles); IF_EQUIPPED is a soft,
 *  driver-judgment flag (the item applies only when the vehicle actually has that equipment). */
enum class ApplicabilityFlag { COMBO, AIR, IF_EQUIPPED }

data class InspectionItem(
    val id: ContentId, val categoryId: ContentId, val title: String, val inspectFor: String,
    val sampleDefects: String, val sequence: Int, val isSample: Boolean = true,
    val sourceCitation: String = "",
    val verificationStatus: VerificationStatus = VerificationStatus.UNVERIFIED,
    val applicabilityFlags: Set<ApplicabilityFlag> = emptySet(),
)

/** The driver's inspection configuration — the predicates the hard applicability flags test
 *  against. Kept separate from the safety-critical routing [VehicleProfile]. */
data class InspectionConfig(val isCombination: Boolean = false, val hasAirBrakes: Boolean = false)

/** Result of testing one item against an [InspectionConfig]. APPLIES and IF_EQUIPPED are both
 *  shown to the driver; only IF_EQUIPPED is optional (skip when not fitted). NOT_APPLICABLE
 *  items are filtered out of the active list for this configuration. */
enum class ItemApplicability { APPLIES, IF_EQUIPPED, NOT_APPLICABLE }

object InspectionApplicability {
    /**
     * Hard flags (COMBO, AIR) are include/exclude filters evaluated from the config. IF_EQUIPPED
     * is a *soft* flag: it never excludes an item — hiding equipment the vehicle might actually
     * have is the wrong failure mode — it only marks the item optional so the driver can skip it
     * when not fitted. An item with no flags always APPLIES.
     */
    fun evaluate(flags: Set<ApplicabilityFlag>, config: InspectionConfig): ItemApplicability {
        val hardSatisfied = (flags - ApplicabilityFlag.IF_EQUIPPED).all { flag ->
            when (flag) {
                ApplicabilityFlag.COMBO -> config.isCombination
                ApplicabilityFlag.AIR -> config.hasAirBrakes
                ApplicabilityFlag.IF_EQUIPPED -> true
            }
        }
        return when {
            !hardSatisfied -> ItemApplicability.NOT_APPLICABLE
            ApplicabilityFlag.IF_EQUIPPED in flags -> ItemApplicability.IF_EQUIPPED
            else -> ItemApplicability.APPLIES
        }
    }
}
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

data class ExamScore(val correct: Int, val total: Int) {
    val percent get() = if (total == 0) 0 else correct * 100 / total
}

/** A completed mock-exam record for local test history. Deliberately carries NO readiness or
 *  pass/fail judgement — only a factual score — per the no-false-claims safety constraint. */
data class ExamResult(
    val id: String, val categoryTitle: String, val score: ExamScore, val completedAtEpochMillis: Long,
)

object MockExamEngine {
    /** A randomized, deterministic-with-seed subset of the pool, capped at the pool size. */
    fun buildExam(pool: List<PracticeQuestion>, size: Int, random: kotlin.random.Random): List<PracticeQuestion> {
        if (size <= 0 || pool.isEmpty()) return emptyList()
        return pool.shuffled(random).take(size.coerceAtMost(pool.size))
    }

    /** `answers` maps question id -> chosen answer id (absent = unanswered = incorrect). */
    fun score(questions: List<PracticeQuestion>, answers: Map<ContentId, ContentId>): ExamScore =
        ExamScore(questions.count { answers[it.id] == it.correctAnswerId }, questions.size)

    fun missed(questions: List<PracticeQuestion>, answers: Map<ContentId, ContentId>): List<PracticeQuestion> =
        questions.filter { answers[it.id] != it.correctAnswerId }
}
data class DailyQuestion(
    val id: ContentId, val prompt: String, val answers: List<AnswerChoice>,
    val correctAnswerId: ContentId, val explanation: String, val isSample: Boolean = true,
)

/** Pure logic for the daily safety question: deterministic one-per-day selection and streak
 *  counting. `epochDay` is a UTC day index (millis / 86_400_000). */
object DailySafety {
    fun selectForDay(pool: List<DailyQuestion>, epochDay: Long): DailyQuestion? {
        if (pool.isEmpty()) return null
        // Stable ordering by id so selection doesn't depend on storage iteration order.
        val ordered = pool.sortedBy { it.id.value }
        val index = ((epochDay % ordered.size) + ordered.size) % ordered.size
        return ordered[index.toInt()]
    }

    /** Consecutive completed days ending at `today`. If today isn't completed yet, the streak
     *  counts back from yesterday, so it isn't reported as broken until a day is truly missed. */
    fun currentStreak(completedDays: Set<Long>, today: Long): Int {
        if (completedDays.isEmpty()) return 0
        var day = if (today in completedDays) today else today - 1
        var streak = 0
        while (day in completedDays) { streak++; day-- }
        return streak
    }
}
/** One truck-stop / rest-area directory record (Track C). Amenity fields are three-state:
 *  true/false when the source actually recorded the fact, null when unknown. Unknown is never
 *  displayed or filtered as absent — missing data is unknown, not proof of absence. */
data class TruckStop(
    val id: ContentId,
    val name: String,
    val state: String,
    val highway: String,
    val latitude: Double,
    val longitude: Double,
    val truckParkingSpaces: Int? = null,
    val hasDiesel: Boolean? = null,
    val hasShowers: Boolean? = null,
    val hasFood: Boolean? = null,
    val hasRepair: Boolean? = null,
    val isSample: Boolean = true,
    val sourceCitation: String = "",
    val verificationStatus: VerificationStatus = VerificationStatus.UNVERIFIED,
    /** Human-readable provenance of the imported dataset (e.g. compilation date), shown in
     *  the directory so drivers can judge how stale a record may be. */
    val datasetVintage: String = "",
)

enum class TruckStopAmenity { DIESEL, SHOWERS, FOOD, REPAIR }

data class TruckStopFilter(
    val query: String = "",
    /** Two-letter state filter; null means all states. */
    val state: String? = null,
    /** Amenities the driver requires. Only stops with the amenity recorded true match. */
    val requiredAmenities: Set<TruckStopAmenity> = emptySet(),
)

data class TruckStopSearchResult(
    val matches: List<TruckStop>,
    /** Stops that matched the query/state but were hidden ONLY because at least one required
     *  amenity is unknown (null, never false). Surfaced in the UI so amenity filters never
     *  silently bury records whose data is merely missing. */
    val hiddenUnknownCount: Int,
)

/** Pure offline search/filter logic for the truck-stop directory. */
object TruckStopSearch {
    fun amenityValue(stop: TruckStop, amenity: TruckStopAmenity): Boolean? = when (amenity) {
        TruckStopAmenity.DIESEL -> stop.hasDiesel
        TruckStopAmenity.SHOWERS -> stop.hasShowers
        TruckStopAmenity.FOOD -> stop.hasFood
        TruckStopAmenity.REPAIR -> stop.hasRepair
    }

    /**
     * A stop matches when it satisfies the text query (name, highway, or state,
     * case-insensitive), the state filter, and has every required amenity recorded true.
     * A stop whose required amenities are only *unknown* (none false) is excluded from
     * matches but counted in [TruckStopSearchResult.hiddenUnknownCount]; a stop with a
     * required amenity recorded false is an ordinary non-match. Results are ordered by
     * state then name for stable, scannable display.
     *
     * Single pass with no per-stop allocations: this re-runs on every query keystroke and
     * must stay smooth once the directory holds the full national dataset (8,000+ rows).
     */
    fun search(stops: List<TruckStop>, filter: TruckStopFilter): TruckStopSearchResult {
        val query = filter.query.trim()
        val matches = mutableListOf<TruckStop>()
        var hiddenUnknown = 0
        for (stop in stops) {
            if (filter.state != null && !stop.state.equals(filter.state, ignoreCase = true)) continue
            if (query.isNotEmpty() &&
                !stop.name.contains(query, ignoreCase = true) &&
                !stop.highway.contains(query, ignoreCase = true) &&
                !stop.state.contains(query, ignoreCase = true)
            ) continue
            var allRecordedTrue = true
            var anyRecordedFalse = false
            for (amenity in filter.requiredAmenities) {
                when (amenityValue(stop, amenity)) {
                    true -> Unit
                    false -> { anyRecordedFalse = true; allRecordedTrue = false }
                    null -> allRecordedTrue = false
                }
                if (anyRecordedFalse) break
            }
            when {
                allRecordedTrue -> matches += stop
                !anyRecordedFalse -> hiddenUnknown++
            }
        }
        matches.sortWith(compareBy({ it.state }, { it.name }))
        return TruckStopSearchResult(matches, hiddenUnknown)
    }
}

/** Result of parsing a truck-stop content file: the usable records plus a count of
 *  structurally-broken records that were skipped (surfaced so imports can be audited —
 *  a silent drop would misreport dataset coverage). */
data class TruckStopDataset(
    val stops: List<TruckStop>,
    val skippedRecords: Int,
)

/**
 * Parser for Last Wagon's own truck-stop content JSON — the app-side format of the
 * versioned content pipeline. External datasets (e.g. the NTAD Truck Stop Parking layer)
 * are converted to this schema at content-preparation time, after their license and field
 * schema pass verification; the app never parses provider formats directly.
 *
 * Envelope: `schema_version` (must equal [SUPPORTED_SCHEMA_VERSION]), `dataset`
 * (`citation`, `vintage`, `verification`, `sample`) applied to every record, and `stops`.
 * Per record: `id`, `name`, `state`, `lat`, `lon` are required; `highway`,
 * `parking_spaces`, and the amenity booleans (`diesel`, `showers`, `food`, `repair`) are
 * optional — a missing or null amenity stays null (unknown), never false. A structurally
 * broken record — or one whose `id` duplicates an earlier record, which Room's REPLACE
 * insert would otherwise silently collapse — is skipped and counted, never fatal; an
 * unparseable envelope or wrong schema version yields an empty dataset.
 */
object TruckStopContent {
    const val SUPPORTED_SCHEMA_VERSION = 1

    fun parse(json: String): TruckStopDataset {
        val root = runCatching { Json.parseToJsonElement(json) }.getOrNull() as? JsonObject
            ?: return TruckStopDataset(emptyList(), 0)
        val version = (root["schema_version"] as? JsonPrimitive)?.intOrNull
        if (version != SUPPORTED_SCHEMA_VERSION) return TruckStopDataset(emptyList(), 0)
        val dataset = root["dataset"] as? JsonObject
        val citation = dataset.string("citation").orEmpty()
        val vintage = dataset.string("vintage").orEmpty()
        val verification = VerificationStatus.entries
            .firstOrNull { it.name == dataset.string("verification") }
            ?: VerificationStatus.UNVERIFIED
        val sample = (dataset?.get("sample") as? JsonPrimitive)?.booleanOrNull ?: true
        val records = root["stops"] as? JsonArray ?: return TruckStopDataset(emptyList(), 0)
        val stops = mutableListOf<TruckStop>()
        val seenIds = HashSet<String>()
        var skipped = 0
        for (element in records) {
            val stop = parseStop(element, citation, vintage, verification, sample)
            if (stop != null && seenIds.add(stop.id.value)) stops += stop else skipped++
        }
        return TruckStopDataset(stops, skipped)
    }

    private fun parseStop(
        element: JsonElement, citation: String, vintage: String,
        verification: VerificationStatus, sample: Boolean,
    ): TruckStop? {
        val record = element as? JsonObject ?: return null
        val id = record.string("id")?.takeIf { it.isNotBlank() } ?: return null
        val name = record.string("name")?.takeIf { it.isNotBlank() } ?: return null
        val state = record.string("state")?.takeIf { it.isNotBlank() } ?: return null
        val latitude = (record["lat"] as? JsonPrimitive)?.doubleOrNull ?: return null
        val longitude = (record["lon"] as? JsonPrimitive)?.doubleOrNull ?: return null
        if (!GeoPoint(latitude, longitude).isValid) return null
        return TruckStop(
            id = ContentId(id),
            name = name,
            state = state,
            highway = record.string("highway").orEmpty(),
            latitude = latitude,
            longitude = longitude,
            truckParkingSpaces = (record["parking_spaces"] as? JsonPrimitive)?.intOrNull
                ?.takeIf { it >= 0 },
            hasDiesel = record.optionalBoolean("diesel"),
            hasShowers = record.optionalBoolean("showers"),
            hasFood = record.optionalBoolean("food"),
            hasRepair = record.optionalBoolean("repair"),
            isSample = sample,
            sourceCitation = citation,
            verificationStatus = verification,
            datasetVintage = vintage,
        )
    }

    private fun JsonObject?.string(key: String): String? =
        (this?.get(key) as? JsonPrimitive)?.takeIf { it.isString }?.contentOrNull

    private fun JsonObject.optionalBoolean(key: String): Boolean? =
        (this[key] as? JsonPrimitive)?.booleanOrNull
}

enum class ThemePreference { DARK, LIGHT, SYSTEM }
data class UserPreferences(
    val theme: ThemePreference = ThemePreference.DARK,
    val reduceMotion: Boolean = false,
    val largeText: Boolean = false,
    /** Driver-supplied OpenRouteService key; blank means use the build-time key. */
    val orsApiKeyOverride: String = "",
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
    /** The full pool of practice questions in a category, for building randomized mock exams. */
    suspend fun getPracticeQuestions(categoryId: ContentId): List<PracticeQuestion>
    suspend fun getDailyQuestion(): DailyQuestion?
    /** The full pool of daily questions, for deterministic one-per-day selection. */
    suspend fun getDailyQuestions(): List<DailyQuestion>
    /** Truck-stop directory records, ordered by state then name. */
    fun observeTruckStops(): Flow<List<TruckStop>>
}
interface ProgressRepository {
    fun observeInspectionProgress(): Flow<InspectionProgress>
    fun observeProgressSnapshot(): Flow<ProgressSnapshot>
    suspend fun setInspectionItemComplete(itemId: ContentId, complete: Boolean)
    suspend fun recordPracticeAnswer(questionId: ContentId, result: AnswerResult)
    suspend fun completeDailyQuestion(questionId: ContentId, correct: Boolean)
    /** UTC day indices on which a daily question has been completed (for streak counting). */
    fun observeCompletedDailyDays(): Flow<Set<Long>>
    /** Records a completed mock exam in local history. */
    suspend fun recordExamResult(categoryTitle: String, score: ExamScore)
    /** Local mock-exam history, most recent first. */
    fun observeExamHistory(): Flow<List<ExamResult>>
    suspend fun resetAllProgress()
}
interface PreferencesRepository {
    val preferences: Flow<UserPreferences>
    suspend fun setTheme(theme: ThemePreference)
    suspend fun setReduceMotion(enabled: Boolean)
    suspend fun setLargeText(enabled: Boolean)
    suspend fun setOrsApiKeyOverride(key: String)
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

data class RouteReview(
    val routeRequestId: String,
    val vehicleProfileConfirmedAtEpochMillis: Long,
    val reviewedAtEpochMillis: Long,
    val acknowledgmentVersion: Int = 1,
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
    val review: RouteReview? = null,
)

val CalculatedRoute.hasCurrentDriverReview: Boolean get() = review?.let {
    it.routeRequestId == provenance.requestId &&
        it.vehicleProfileConfirmedAtEpochMillis == request.vehicleProfile.confirmedAtEpochMillis &&
        it.reviewedAtEpochMillis > 0 && it.acknowledgmentVersion == 1
} == true

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
    suspend fun recordReview(review: RouteReview): Boolean
    suspend fun clear()
}
