package com.trkrhlpr.feature.learning

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.FactCheck
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trkrhlpr.core.designsystem.*
import com.trkrhlpr.core.model.*
import kotlinx.coroutines.launch

@Composable fun InspectionScreen(
    contentRepository: ContentRepository,
    progressRepository: ProgressRepository,
    modifier: Modifier = Modifier,
) {
    val categories by contentRepository.observeInspectionCategories().collectAsState(initial = emptyList())
    val items by contentRepository.observeInspectionItems().collectAsState(initial = emptyList())
    val progress by progressRepository.observeInspectionProgress().collectAsState(initial = InspectionProgress())
    var mode by rememberSaveable { mutableStateOf<InspectionMode?>(null) }
    var expandedId by rememberSaveable { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    AnimatedContent(mode, label = "inspectionMode") { activeMode ->
        if (activeMode == null) {
            LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(TrkrSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(TrkrSpacing.md)) {
                item { SectionHeader("Pre-trip", "Inspection workspace",
                    "Representative sample only — official 131-point content requires source review.") }
                item {
                    FeatureTile("Study mode", "Browse sample categories and inspect item details.",
                        Icons.AutoMirrored.Rounded.MenuBook, TrkrColors.DashboardBlue, onClick = { mode = InspectionMode.STUDY })
                }
                item {
                    FeatureTile("Real inspection mode", "Walk the sample sequence and track completion.",
                        Icons.AutoMirrored.Rounded.FactCheck, TrkrColors.MarkerAmber, onClick = { mode = InspectionMode.INSPECTION })
                }
                item {
                    Text("REPRESENTATIVE CATEGORIES", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary)
                }
                items(categories) { category ->
                    TrkrCard(Modifier.fillMaxWidth()) {
                        Text(category.sequence.toString().padStart(2, '0'), color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelLarge)
                        Text(category.title, style = MaterialTheme.typography.titleLarge)
                        Text(category.itemCount.toString() + " sample item",
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(TrkrSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(TrkrSpacing.md)) {
                item {
                    TextButton(onClick = { mode = null }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, null); Text("Modes")
                    }
                    SectionHeader(if (activeMode == InspectionMode.STUDY) "Study mode" else "Inspection mode",
                        if (activeMode == InspectionMode.STUDY) "Learn the sequence" else "Sample walk-through",
                        "Sample content — not an authoritative inspection procedure.")
                }
                item { TrkrProgress(progress.fraction, progress.completedCount.toString() + " of " + progress.totalItems) }
                items(items, key = { it.id.value }) { item ->
                    val complete = item.id in progress.completedItemIds
                    ChecklistRow(item.title, item.inspectFor + "\n\nCommon sample conditions\n" + item.sampleDefects,
                        complete, expandedId == item.id.value,
                        onToggleComplete = {
                            scope.launch { progressRepository.setInspectionItemComplete(item.id, !complete) }
                        },
                        onToggleExpanded = {
                            expandedId = if (expandedId == item.id.value) null else item.id.value
                        })
                }
                if (progress.isComplete) {
                    item { StatePanel("Sample inspection complete",
                        "All representative items are marked. A production session will later include timestamps and notes.",
                        Icons.Rounded.TaskAlt, TrkrColors.SignalGreen) }
                }
            }
        }
    }
}

private enum class InspectionMode { STUDY, INSPECTION }

@Composable fun PracticeScreen(
    contentRepository: ContentRepository,
    progressRepository: ProgressRepository,
    modifier: Modifier = Modifier,
) {
    val categories by contentRepository.observeTestCategories().collectAsState(initial = emptyList())
    var selectedCategoryId by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedCategory = categories.firstOrNull { it.id.value == selectedCategoryId }
    var question by remember { mutableStateOf<PracticeQuestion?>(null) }
    var selectedAnswer by rememberSaveable { mutableStateOf<String?>(null) }
    var submitted by rememberSaveable { mutableStateOf(false) }
    var showResult by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(selectedCategory) {
        question = selectedCategory?.let { contentRepository.getPracticeQuestion(it.id) }
        selectedAnswer = null; submitted = false; showResult = false
    }

    AnimatedContent(targetState = Triple(selectedCategory, submitted, showResult), label = "practiceFlow") { state ->
        val category = state.first
        when {
            category == null -> LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(TrkrSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(TrkrSpacing.md)) {
                item { SectionHeader("Knowledge", "CDL practice", "Sample interaction only. No question is authoritative.") }
                items(categories) { value ->
                    TrkrCard(Modifier.fillMaxWidth(), onClick = { selectedCategoryId = value.id.value }) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(value.title, style = MaterialTheme.typography.titleLarge)
                                Text("1 sample question", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(Icons.Rounded.ChevronRight, null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
            showResult -> Column(modifier.fillMaxSize().padding(TrkrSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(TrkrSpacing.lg)) {
                SectionHeader("Session complete", "Result summary", category.title)
                StatePanel(
                    if (selectedAnswer == question?.correctAnswerId?.value) "Sample answer correct" else "Review the explanation",
                    "One representative question recorded locally. Readiness scoring requires approved content and is not available.",
                    Icons.Rounded.MilitaryTech,
                    if (selectedAnswer == question?.correctAnswerId?.value) TrkrColors.SignalGreen else TrkrColors.MarkerAmber)
                Button(onClick = { selectedCategoryId = null }, Modifier.fillMaxWidth().heightIn(min = 56.dp)) {
                    Text("Back to categories")
                }
            }
            else -> {
                val value = question
                if (value == null) LoadingPanel()
                else LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(TrkrSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(TrkrSpacing.md)) {
                    item {
                        TextButton(onClick = { selectedCategoryId = null }) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, null); Text("Categories")
                        }
                        SectionHeader(category.title, "Sample question", "Not official CDL content")
                    }
                    item { TrkrCard(Modifier.fillMaxWidth()) { Text(value.prompt, style = MaterialTheme.typography.titleLarge) } }
                    items(value.answers) { answer ->
                        val isSelected = selectedAnswer == answer.id.value
                        AnswerOption(answer.text, isSelected,
                            correct = if (!submitted) null else when {
                                answer.id == value.correctAnswerId -> true
                                isSelected -> false
                                else -> null
                            }, enabled = !submitted, onClick = { selectedAnswer = answer.id.value })
                    }
                    if (submitted) {
                        item {
                            StatePanel("Why this answer?", value.explanation, Icons.Rounded.Lightbulb,
                                if (selectedAnswer == value.correctAnswerId.value) TrkrColors.SignalGreen else TrkrColors.MarkerAmber,
                                "View result") { showResult = true }
                        }
                    } else {
                        item {
                            Button(
                                enabled = selectedAnswer != null,
                                onClick = {
                                    val answerId = ContentId(requireNotNull(selectedAnswer))
                                    scope.launch {
                                        progressRepository.recordPracticeAnswer(value.id,
                                            AnswerResult(answerId, answerId == value.correctAnswerId, value.explanation))
                                        submitted = true
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                            ) { Text("Check answer") }
                        }
                    }
                }
            }
        }
    }
}

@Composable fun DailyQuestionScreen(
    contentRepository: ContentRepository,
    progressRepository: ProgressRepository,
    modifier: Modifier = Modifier,
) {
    var question by remember { mutableStateOf<DailyQuestion?>(null) }
    var selected by rememberSaveable { mutableStateOf<String?>(null) }
    var complete by rememberSaveable { mutableStateOf(false) }
    val progress by progressRepository.observeProgressSnapshot().collectAsState(initial = ProgressSnapshot())
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { question = contentRepository.getDailyQuestion() }
    LaunchedEffect(progress.dailyCompleted) {
        if (progress.dailyCompleted) complete = true
    }
    val value = question
    if (value == null) {
        Box(modifier.fillMaxSize().padding(TrkrSpacing.lg)) { LoadingPanel("Loading today's local question") }
        return
    }
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(TrkrSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(TrkrSpacing.md)) {
        item { SectionHeader("Daily safety", "One deliberate minute", "Representative local sample • no network required") }
        item {
            TrkrCard(Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.spacedBy(TrkrSpacing.sm)) {
                    Icon(Icons.Rounded.WbSunny, null, tint = TrkrColors.MarkerAmber)
                    Text("TODAY'S SAMPLE", style = MaterialTheme.typography.labelLarge,
                        color = TrkrColors.MarkerAmber)
                }
                Text(value.prompt, style = MaterialTheme.typography.headlineMedium)
            }
        }
        items(value.answers) { answer ->
            val isSelected = selected == answer.id.value
            AnswerOption(answer.text, isSelected,
                correct = if (!complete) null else when {
                    answer.id == value.correctAnswerId -> true; isSelected -> false; else -> null
                }, enabled = !complete, onClick = { selected = answer.id.value })
        }
        item {
            if (complete) {
                StatePanel("Daily check recorded", value.explanation, Icons.Rounded.TaskAlt,
                    TrkrColors.SignalGreen)
            } else {
                Button(enabled = selected != null, onClick = {
                    scope.launch {
                        progressRepository.completeDailyQuestion(value.id, selected == value.correctAnswerId.value)
                        complete = true
                    }
                }, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)) { Text("Submit answer") }
            }
        }
    }
}
