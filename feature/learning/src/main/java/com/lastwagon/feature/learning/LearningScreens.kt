package com.lastwagon.feature.learning

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastwagon.core.designsystem.*
import com.lastwagon.core.model.*
import kotlinx.coroutines.launch

@Composable fun InspectionScreen(
    contentRepository: ContentRepository,
    progressRepository: ProgressRepository,
    modifier: Modifier = Modifier,
) {
    val categories by contentRepository.observeInspectionCategories().collectAsStateWithLifecycle(
        initialValue = emptyList(),
    )
    val items by contentRepository.observeInspectionItems().collectAsStateWithLifecycle(
        initialValue = emptyList(),
    )
    val progress by progressRepository.observeInspectionProgress().collectAsStateWithLifecycle(
        initialValue = InspectionProgress(),
    )
    var mode by rememberSaveable { mutableStateOf<InspectionMode?>(null) }
    var expandedId by rememberSaveable { mutableStateOf<String?>(null) }
    // Inspection-Mode vehicle configuration used to filter items via the applicability flags.
    // Kept local (not linked to the safety-critical routing VehicleProfile); persistence of the
    // selection is deferred to the local-progress unit.
    var isCombination by rememberSaveable { mutableStateOf(false) }
    var hasAirBrakes by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AnimatedContent(mode, label = "inspectionMode") { activeMode ->
        if (activeMode == null) {
            LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(WagonSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(WagonSpacing.md)) {
                item { SectionHeader("Pre-trip", "Inspection workspace",
                    "Representative sample only — official 132-point content requires source review.") }
                item {
                    FeatureTile("Study mode", "Browse sample categories and inspect item details.",
                        Icons.AutoMirrored.Rounded.MenuBook, WagonColors.DashboardBlue, onClick = { mode = InspectionMode.STUDY })
                }
                item {
                    FeatureTile("Real inspection mode", "Walk the sample sequence and track completion.",
                        Icons.AutoMirrored.Rounded.FactCheck, WagonColors.MarkerAmber, onClick = { mode = InspectionMode.INSPECTION })
                }
                item {
                    Text("REPRESENTATIVE CATEGORIES", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary)
                }
                items(categories) { category ->
                    WagonCard(Modifier.fillMaxWidth()) {
                        Text(category.sequence.toString().padStart(2, '0'), color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelLarge)
                        Text(category.title, style = MaterialTheme.typography.titleLarge)
                        Text(category.itemCount.toString() + " sample item",
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else if (activeMode == InspectionMode.STUDY) {
            StudyModeList(categories, items, expandedId,
                onToggleExpanded = { id -> expandedId = if (expandedId == id) null else id },
                onBack = { mode = null }, modifier)
        } else {
            val config = InspectionConfig(isCombination, hasAirBrakes)
            val evaluated = items.map { it to InspectionApplicability.evaluate(it.applicabilityFlags, config) }
            val applies = evaluated.filter { it.second == ItemApplicability.APPLIES }.map { it.first }
            val ifEquipped = evaluated.filter { it.second == ItemApplicability.IF_EQUIPPED }.map { it.first }
            val notApplicable = evaluated.count { it.second == ItemApplicability.NOT_APPLICABLE }
            val requiredComplete = applies.count { it.id in progress.completedItemIds }
            val requiredFraction = if (applies.isEmpty()) 0f else requiredComplete.toFloat() / applies.size
            val onComplete: (InspectionItem, Boolean) -> Unit = { item, complete ->
                scope.launch { progressRepository.setInspectionItemComplete(item.id, !complete) }
            }
            val onExpand: (String) -> Unit = { id -> expandedId = if (expandedId == id) null else id }
            LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(WagonSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(WagonSpacing.md)) {
                item {
                    TextButton(onClick = { mode = null }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, null); Text("Modes")
                    }
                    SectionHeader("Inspection mode", "Sample walk-through",
                        "Sample content — not an authoritative inspection procedure.")
                }
                item {
                    InspectionConfigCard(isCombination, { isCombination = it },
                        hasAirBrakes, { hasAirBrakes = it })
                }
                item {
                    WagonProgress(requiredFraction,
                        requiredComplete.toString() + " of " + applies.size + " applicable")
                }
                if (applies.isEmpty() && ifEquipped.isEmpty()) {
                    item {
                        StatePanel("No applicable sample items",
                            "This configuration filters out every sample item. Adjust the vehicle configuration above.",
                            Icons.Rounded.Info, WagonColors.MarkerAmber)
                    }
                }
                checklistItems(applies, progress.completedItemIds, expandedId, false, onComplete, onExpand)
                if (ifEquipped.isNotEmpty()) {
                    item {
                        Text("IF EQUIPPED — VERIFY IF FITTED", style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary)
                    }
                    checklistItems(ifEquipped, progress.completedItemIds, expandedId, true, onComplete, onExpand)
                }
                if (notApplicable > 0) {
                    item {
                        Text(notApplicable.toString() + " sample item" + (if (notApplicable == 1) "" else "s") +
                            " hidden — not applicable to this configuration.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                if (applies.isNotEmpty() && requiredComplete == applies.size) {
                    item { StatePanel("Sample inspection complete",
                        "All applicable representative items are marked. A production session will later include timestamps and notes.",
                        Icons.Rounded.TaskAlt, WagonColors.SignalGreen) }
                }
            }
        }
    }
}

private enum class InspectionMode { STUDY, INSPECTION }

/** Emits completion rows for a set of inspection items. `optional` marks IF_EQUIPPED items so
 *  the driver can skip them when the equipment isn't fitted. */
private fun LazyListScope.checklistItems(
    items: List<InspectionItem>,
    completedIds: Set<ContentId>,
    expandedId: String?,
    optional: Boolean,
    onComplete: (InspectionItem, Boolean) -> Unit,
    onExpand: (String) -> Unit,
) {
    items(items, key = { it.id.value }) { item ->
        val complete = item.id in completedIds
        val prefix = if (optional) "If equipped: skip if your vehicle isn't fitted with this.\n\n" else ""
        ChecklistRow(item.title,
            prefix + item.inspectFor + "\n\nCommon sample conditions\n" + item.sampleDefects,
            complete, expandedId == item.id.value,
            onToggleComplete = { onComplete(item, complete) },
            onToggleExpanded = { onExpand(item.id.value) })
    }
}

@Composable private fun InspectionConfigCard(
    isCombination: Boolean, onCombination: (Boolean) -> Unit,
    hasAirBrakes: Boolean, onAirBrakes: (Boolean) -> Unit,
) {
    WagonCard(Modifier.fillMaxWidth()) {
        Text("VEHICLE CONFIGURATION", style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary)
        Text("Filters the sample checklist by applicability. Not linked to the routing vehicle profile.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        ConfigToggle("Combination vehicle (towing a trailer)", isCombination, onCombination)
        ConfigToggle("Air brakes", hasAirBrakes, onAirBrakes)
    }
}

@Composable private fun ConfigToggle(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WagonSpacing.sm)) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Switch(checked, onChange)
    }
}

/**
 * Study Mode (Track A unit 2): read-only browse of the checklist grouped by section, showing
 * each item's sequence, what to inspect for, common sample conditions, its verification status
 * and applicability flags, and its source citation. No completion toggles — that is Inspection
 * Mode's job.
 */
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable private fun StudyModeList(
    categories: List<InspectionCategory>,
    items: List<InspectionItem>,
    expandedId: String?,
    onToggleExpanded: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemsByCategory = items.groupBy { it.categoryId }
    val orderedCategories = categories.sortedBy { it.sequence }
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(WagonSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(WagonSpacing.md)) {
        item(key = "study-header") {
            TextButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, null); Text("Modes")
            }
            SectionHeader("Study mode", "Browse by section",
                "Read-only sample content — not an authoritative inspection procedure. Tap an item for detail.")
        }
        orderedCategories.forEach { category ->
            val sectionItems = itemsByCategory[category.id].orEmpty().sortedBy { it.sequence }
            item(key = "cat-" + category.id.value) { StudySectionHeader(category, sectionItems.size) }
            items(sectionItems, key = { it.id.value }) { item ->
                StudyItemCard(item, expandedId == item.id.value) { onToggleExpanded(item.id.value) }
            }
        }
    }
}

@Composable private fun StudySectionHeader(category: InspectionCategory, count: Int) {
    Row(Modifier.fillMaxWidth().padding(top = WagonSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WagonSpacing.sm)) {
        Text(category.sequence.toString().padStart(2, '0'),
            style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
        Text(category.title, Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
        Text("$count " + if (count == 1) "item" else "items",
            style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable private fun StudyItemCard(item: InspectionItem, expanded: Boolean, onToggle: () -> Unit) {
    WagonCard(Modifier.fillMaxWidth(), onClick = onToggle) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(WagonSpacing.sm)) {
            Text(item.sequence.toString().padStart(2, '0'),
                style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text(item.title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            Icon(if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                if (expanded) "Collapse item" else "Expand item")
        }
        androidx.compose.foundation.layout.FlowRow(
            horizontalArrangement = Arrangement.spacedBy(WagonSpacing.xs),
            verticalArrangement = Arrangement.spacedBy(WagonSpacing.xs)) {
            val (statusLabel, statusColor) = verificationDisplay(item.verificationStatus)
            WagonTag(statusLabel, statusColor)
            item.applicabilityFlags.sortedBy { it.name }.forEach { WagonTag(flagLabel(it), WagonColors.DashboardBlue) }
        }
        if (expanded) {
            StudyDetail("Inspect for", item.inspectFor)
            StudyDetail("Common sample conditions", item.sampleDefects)
            StudyDetail("Source", item.sourceCitation.ifBlank { "Not cited (sample)" })
        }
    }
}

@Composable private fun StudyDetail(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(WagonSpacing.xxs)) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun flagLabel(flag: ApplicabilityFlag): String = when (flag) {
    ApplicabilityFlag.COMBO -> "Combination"
    ApplicabilityFlag.AIR -> "Air brakes"
    ApplicabilityFlag.IF_EQUIPPED -> "If equipped"
}

private fun verificationDisplay(status: VerificationStatus): Pair<String, Color> = when (status) {
    VerificationStatus.VERIFIED -> "Verified" to WagonColors.SignalGreen
    VerificationStatus.PARTIAL -> "Partial" to WagonColors.MarkerAmber
    VerificationStatus.UNVERIFIED -> "Unverified" to WagonColors.Steel500
}

@Composable fun PracticeScreen(
    contentRepository: ContentRepository,
    progressRepository: ProgressRepository,
    modifier: Modifier = Modifier,
) {
    val categories by contentRepository.observeTestCategories().collectAsStateWithLifecycle(
        initialValue = emptyList(),
    )
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
            category == null -> LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(WagonSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(WagonSpacing.md)) {
                item { SectionHeader("Knowledge", "CDL practice", "Sample interaction only. No question is authoritative.") }
                items(categories) { value ->
                    WagonCard(Modifier.fillMaxWidth(), onClick = { selectedCategoryId = value.id.value }) {
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
            showResult -> Column(modifier.fillMaxSize().padding(WagonSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(WagonSpacing.lg)) {
                SectionHeader("Session complete", "Result summary", category.title)
                StatePanel(
                    if (selectedAnswer == question?.correctAnswerId?.value) "Sample answer correct" else "Review the explanation",
                    "One representative question recorded locally. Readiness scoring requires approved content and is not available.",
                    Icons.Rounded.MilitaryTech,
                    if (selectedAnswer == question?.correctAnswerId?.value) WagonColors.SignalGreen else WagonColors.MarkerAmber)
                Button(onClick = { selectedCategoryId = null }, Modifier.fillMaxWidth().heightIn(min = 56.dp)) {
                    Text("Back to categories")
                }
            }
            else -> {
                val value = question
                if (value == null) LoadingPanel()
                else LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(WagonSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(WagonSpacing.md)) {
                    item {
                        TextButton(onClick = { selectedCategoryId = null }) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, null); Text("Categories")
                        }
                        SectionHeader(category.title, "Sample question", "Not official CDL content")
                    }
                    item { WagonCard(Modifier.fillMaxWidth()) { Text(value.prompt, style = MaterialTheme.typography.titleLarge) } }
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
                                if (selectedAnswer == value.correctAnswerId.value) WagonColors.SignalGreen else WagonColors.MarkerAmber,
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
    val progress by progressRepository.observeProgressSnapshot().collectAsStateWithLifecycle(
        initialValue = ProgressSnapshot(),
    )
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        val pool = contentRepository.getDailyQuestions()
        question = DailySafety.selectForDay(pool, System.currentTimeMillis() / 86_400_000L)
    }
    LaunchedEffect(progress.dailyCompleted) {
        if (progress.dailyCompleted) complete = true
    }
    val value = question
    if (value == null) {
        Box(modifier.fillMaxSize().padding(WagonSpacing.lg)) { LoadingPanel("Loading today's local question") }
        return
    }
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(WagonSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(WagonSpacing.md)) {
        item { SectionHeader("Daily safety", "One deliberate minute", "Representative local sample • no network required") }
        item {
            WagonCard(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(WagonSpacing.sm)) {
                    Icon(Icons.Rounded.WbSunny, null, tint = WagonColors.MarkerAmber)
                    Text("TODAY'S SAMPLE", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge,
                        color = WagonColors.MarkerAmber)
                    WagonTag(
                        if (progress.dailyStreak > 0) "Streak " + progress.dailyStreak else "No streak yet",
                        if (progress.dailyStreak > 0) WagonColors.SignalGreen else WagonColors.Steel500)
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
                    WagonColors.SignalGreen)
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
