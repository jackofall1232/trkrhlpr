package com.trkrhlpr.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.AltRoute
import androidx.compose.material.icons.automirrored.rounded.FactCheck
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trkrhlpr.core.designsystem.*
import com.trkrhlpr.core.model.*
import kotlinx.coroutines.launch

@Composable fun HomeScreen(
    onInspection: () -> Unit, onPractice: () -> Unit, onDaily: () -> Unit,
    onProgress: () -> Unit, onRouting: () -> Unit, modifier: Modifier = Modifier,
) {
    IndustrialBackdrop(modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(156.dp),
            contentPadding = PaddingValues(TrkrSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(TrkrSpacing.md),
            verticalArrangement = Arrangement.spacedBy(TrkrSpacing.md),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(Modifier.padding(bottom = TrkrSpacing.sm)) {
                    SectionHeader("Driver readiness", "Own the road ahead.",
                        "Inspection, knowledge, and daily safety tools that work offline.")
                    Spacer(Modifier.height(TrkrSpacing.lg))
                    TrkrCard(Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(TrkrSpacing.sm)) {
                            Icon(Icons.Rounded.OfflineBolt, null, tint = TrkrColors.SignalGreen)
                            Column {
                                Text("READY OFFLINE", style = MaterialTheme.typography.labelLarge,
                                    color = TrkrColors.SignalGreen)
                                Text("Your core tools and progress stay on this device.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
            item { FeatureTile("Pre-trip inspection", "Study the sequence or run a live check.",
                Icons.AutoMirrored.Rounded.FactCheck, TrkrColors.MarkerAmber, onClick = onInspection) }
            item { FeatureTile("CDL practice", "Representative question flow and local results.",
                Icons.Rounded.Quiz, TrkrColors.DashboardBlue, onClick = onPractice) }
            item { FeatureTile("Daily safety", "One focused scenario for today.",
                Icons.Rounded.WbSunny, TrkrColors.SignalGreen, badge = "Today", onClick = onDaily) }
            item { FeatureTile("Progress", "Inspection, practice, and daily activity.",
                Icons.Rounded.QueryStats, Color(0xFFB49CFF), onClick = onProgress) }
            item { FeatureTile("Truck stops", "National directory — future roadmap.",
                Icons.Rounded.LocalGasStation, TrkrColors.Steel500, enabled = false,
                badge = "Later", onClick = {}) }
            item { FeatureTile("Route map", "Read-only map preview — no routing yet.",
                Icons.AutoMirrored.Rounded.AltRoute, TrkrColors.DashboardBlue,
                badge = "Preview", onClick = onRouting) }
        }
    }
}

@Composable fun ProgressScreen(
    repository: ProgressRepository,
    onResetComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress by repository.observeProgressSnapshot().collectAsStateWithLifecycle(
        initialValue = ProgressSnapshot(),
    )
    var showReset by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(TrkrSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(TrkrSpacing.lg)) {
        SectionHeader("Local record", "Progress", "Stored only on this device.")
        TrkrCard(Modifier.fillMaxWidth()) {
            TrkrProgress(
                if (progress.inspectionTotal == 0) 0f else progress.inspectionCompleted.toFloat() / progress.inspectionTotal,
                "Inspection items")
            Text(progress.inspectionCompleted.toString() + " of " + progress.inspectionTotal + " complete",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(TrkrSpacing.md)) {
            Metric("PRACTICE", progress.testsCompleted.toString(), "attempts", Modifier.weight(1f))
            Metric("ACCURACY", progress.testAccuracyPercent.toString() + "%", "sample", Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(TrkrSpacing.md)) {
            Metric("DAILY", if (progress.dailyCompleted) "DONE" else "OPEN", "today", Modifier.weight(1f))
            Metric("STREAK", progress.dailyStreak.toString(), "day", Modifier.weight(1f))
        }
        OutlinedButton(onClick = { showReset = true }, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
            Icon(Icons.Rounded.DeleteOutline, null); Spacer(Modifier.width(8.dp)); Text("Reset local progress")
        }
    }
    if (showReset) {
        AlertDialog(
            onDismissRequest = { showReset = false },
            icon = { Icon(Icons.Rounded.WarningAmber, null) },
            title = { Text("Reset local progress?") },
            text = { Text("Inspection completion, practice attempts, and daily completion will be deleted. Sample learning content stays available.") },
            confirmButton = {
                Button(onClick = {
                    scope.launch { repository.resetAllProgress(); showReset = false; onResetComplete() }
                }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("Reset data") }
            },
            dismissButton = { TextButton(onClick = { showReset = false }) { Text("Cancel") } },
        )
    }
}

@Composable private fun Metric(eyebrow: String, value: String, label: String, modifier: Modifier) {
    TrkrCard(modifier.heightIn(min = 126.dp)) {
        Text(eyebrow, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
        Text(value, style = MaterialTheme.typography.headlineMedium)
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable fun SettingsScreen(
    preferencesRepository: PreferencesRepository,
    appVersion: String,
    modifier: Modifier = Modifier,
) {
    val prefs by preferencesRepository.preferences.collectAsStateWithLifecycle(
        initialValue = UserPreferences(),
    )
    val scope = rememberCoroutineScope()
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(TrkrSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(TrkrSpacing.lg)) {
        SectionHeader("Cab setup", "Settings & about", "Tune readability without an account.")
        TrkrCard(Modifier.fillMaxWidth()) {
            Text("APPEARANCE", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                listOf(ThemePreference.DARK, ThemePreference.LIGHT, ThemePreference.SYSTEM).forEachIndexed { i, theme ->
                    SegmentedButton(
                        selected = prefs.theme == theme,
                        onClick = { scope.launch { preferencesRepository.setTheme(theme) } },
                        shape = SegmentedButtonDefaults.itemShape(i, 3),
                    ) { Text(theme.name.lowercase().replaceFirstChar(Char::uppercase)) }
                }
            }
            SettingSwitch("Reduce motion", "Shortens nonessential interface movement.", prefs.reduceMotion) {
                scope.launch { preferencesRepository.setReduceMotion(it) }
            }
            SettingSwitch("Larger reading text", "Increases body and supporting text.", prefs.largeText) {
                scope.launch { preferencesRepository.setLargeText(it) }
            }
        }
        StatePanel("Safety & education disclaimer",
            "trkrhlpr sample content is not official guidance and does not replace regulations, training, employer procedures, required inspections, or driver judgment.",
            Icons.Rounded.HealthAndSafety, TrkrColors.MarkerAmber)
        TrkrCard(Modifier.fillMaxWidth()) {
            Text("ABOUT", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            Text("trkrhlpr " + appVersion, style = MaterialTheme.typography.titleLarge)
            Text("Native Kotlin • Jetpack Compose • Offline first", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Open-source acknowledgments", style = MaterialTheme.typography.titleMedium)
            Text("A complete dependency and license inventory will be finalized before production release.",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable private fun SettingSwitch(title: String, subtitle: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 64.dp), horizontalArrangement = Arrangement.spacedBy(TrkrSpacing.sm)) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked, onChecked)
    }
}
