package com.lastwagon.core.testing

import com.lastwagon.core.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeContentRepository(
    categories: List<InspectionCategory> = emptyList(),
    items: List<InspectionItem> = emptyList(),
    tests: List<TestCategory> = emptyList(),
    stops: List<TruckStop> = emptyList(),
    var practiceQuestion: PracticeQuestion? = null,
    var dailyQuestion: DailyQuestion? = null,
    var dailyQuestions: List<DailyQuestion> = listOfNotNull(dailyQuestion),
) : ContentRepository {
    val inspectionCategories = MutableStateFlow(categories)
    val inspectionItems = MutableStateFlow(items)
    val testCategories = MutableStateFlow(tests)
    val truckStops = MutableStateFlow(stops)
    override suspend fun ensureSampleContent() = Unit
    override fun observeInspectionCategories() = inspectionCategories
    override fun observeInspectionItems() = inspectionItems
    override fun observeTestCategories() = testCategories
    override suspend fun getPracticeQuestion(categoryId: ContentId) = practiceQuestion
    override suspend fun getPracticeQuestions(categoryId: ContentId) =
        practiceQuestion?.let { listOf(it) } ?: emptyList()
    override suspend fun getDailyQuestion() = dailyQuestion
    override suspend fun getDailyQuestions() = dailyQuestions
    override fun observeTruckStops() = truckStops
}

class FakeProgressRepository(
    inspection: InspectionProgress = InspectionProgress(),
    snapshot: ProgressSnapshot = ProgressSnapshot(),
) : ProgressRepository {
    val inspectionProgress = MutableStateFlow(inspection)
    val progressSnapshot = MutableStateFlow(snapshot)
    val completedDailyDays = MutableStateFlow<Set<Long>>(emptySet())
    val examHistory = MutableStateFlow<List<ExamResult>>(emptyList())
    override fun observeInspectionProgress() = inspectionProgress
    override fun observeProgressSnapshot() = progressSnapshot
    override fun observeCompletedDailyDays() = completedDailyDays
    override fun observeExamHistory() = examHistory
    override suspend fun setInspectionItemComplete(itemId: ContentId, complete: Boolean) {
        inspectionProgress.update {
            it.copy(completedItemIds = if (complete) it.completedItemIds + itemId else it.completedItemIds - itemId)
        }
    }
    override suspend fun recordPracticeAnswer(questionId: ContentId, result: AnswerResult) {
        progressSnapshot.update { current ->
            val attempts = current.testsCompleted + 1
            val correctBefore = current.testsCompleted * current.testAccuracyPercent / 100
            current.copy(
                testsCompleted = attempts,
                testAccuracyPercent = (correctBefore + if (result.isCorrect) 1 else 0) * 100 / attempts,
            )
        }
    }
    override suspend fun completeDailyQuestion(questionId: ContentId, correct: Boolean) {
        progressSnapshot.update { it.copy(dailyCompleted = true, dailyStreak = 1) }
    }
    override suspend fun recordExamResult(categoryTitle: String, score: ExamScore) {
        examHistory.update { it + ExamResult("fake-" + it.size, categoryTitle, score, 0L) }
    }
    override suspend fun resetAllProgress() {
        inspectionProgress.value = InspectionProgress(totalItems = inspectionProgress.value.totalItems)
        progressSnapshot.value = ProgressSnapshot()
        examHistory.value = emptyList()
    }
}

class FakePreferencesRepository(initial: UserPreferences = UserPreferences()) : PreferencesRepository {
    private val state = MutableStateFlow(initial)
    override val preferences = state
    override suspend fun setTheme(theme: ThemePreference) { state.update { it.copy(theme = theme) } }
    override suspend fun setReduceMotion(enabled: Boolean) { state.update { it.copy(reduceMotion = enabled) } }
    override suspend fun setLargeText(enabled: Boolean) { state.update { it.copy(largeText = enabled) } }
    override suspend fun setOrsApiKeyOverride(key: String) {
        state.update { it.copy(orsApiKeyOverride = key.trim()) }
    }
}

class FakeVehicleProfileRepository(initial: VehicleProfile? = null) : VehicleProfileRepository {
    private val state = MutableStateFlow(initial)
    override val profile = state
    override suspend fun save(profile: VehicleProfile) { state.value = profile }
    override suspend fun clear() { state.value = null }
}

class FakeRoutingProvider(
    var result: RouteCalculationResult,
    override val id: String = "fake",
) : RoutingProvider {
    override suspend fun calculate(request: RouteRequest) = result
}

class FakeRouteRepository(initial: CalculatedRoute? = null) : RouteRepository {
    private val state = MutableStateFlow(initial)
    override val lastRoute = state
    override suspend fun save(route: CalculatedRoute) { state.value = route }
    override suspend fun recordReview(review: RouteReview): Boolean {
        val route = state.value ?: return false
        if (route.provenance.requestId != review.routeRequestId ||
            route.request.vehicleProfile.confirmedAtEpochMillis != review.vehicleProfileConfirmedAtEpochMillis
        ) return false
        state.value = route.copy(review = review)
        return true
    }
    override suspend fun clear() { state.value = null }
}
