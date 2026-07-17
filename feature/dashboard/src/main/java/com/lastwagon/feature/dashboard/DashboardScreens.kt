package com.lastwagon.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastwagon.core.designsystem.*
import com.lastwagon.core.model.*
import kotlinx.coroutines.launch

@Composable fun HomeScreen(
    contentRepository: ContentRepository,
    onInspection: () -> Unit, onPractice: () -> Unit, onDaily: () -> Unit,
    onProgress: () -> Unit, onRouting: () -> Unit, onTruckStops: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // The tile's badge follows the installed directory dataset: labeled "Sample" until
    // every record is verified real content, then no badge — no separate UI change to
    // remember at the phase-2 dataset swap.
    val truckStops by contentRepository.observeTruckStops().collectAsStateWithLifecycle(
        initialValue = emptyList(),
    )
    val truckStopsBadge =
        if (truckStops.isNotEmpty() && truckStops.none { it.isSample }) null else "Sample"
    IndustrialBackdrop(modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(156.dp),
            contentPadding = PaddingValues(WagonSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(WagonSpacing.md),
            verticalArrangement = Arrangement.spacedBy(WagonSpacing.md),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(Modifier.padding(bottom = WagonSpacing.sm)) {
                    SectionHeader("Driver readiness", "Own the road ahead.",
                        "Inspection, knowledge, and daily safety tools that work offline.")
                    Spacer(Modifier.height(WagonSpacing.lg))
                    WagonCard(Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(WagonSpacing.sm)) {
                            Icon(Icons.Rounded.OfflineBolt, null, tint = WagonColors.SignalGreen)
                            Column {
                                Text("READY OFFLINE", style = MaterialTheme.typography.labelLarge,
                                    color = WagonColors.SignalGreen)
                                Text("Your core tools and progress stay on this device.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
            item { FeatureTile("Pre-trip inspection", "Study the sequence or run a live check.",
                Icons.AutoMirrored.Rounded.FactCheck, WagonColors.MarkerAmber, onClick = onInspection) }
            item { FeatureTile("CDL practice", "Representative question flow and local results.",
                Icons.Rounded.Quiz, WagonColors.DashboardBlue, onClick = onPractice) }
            item { FeatureTile("Daily safety", "One focused scenario for today.",
                Icons.Rounded.WbSunny, WagonColors.SignalGreen, badge = "Today", onClick = onDaily) }
            item { FeatureTile("Progress", "Inspection, practice, and daily activity.",
                Icons.Rounded.QueryStats, Color(0xFFB49CFF), onClick = onProgress) }
            item { FeatureTile("Truck stops", "Truck-stop directory — search offline.",
                Icons.Rounded.LocalGasStation, WagonColors.Steel500,
                badge = truckStopsBadge, onClick = onTruckStops) }
            item { FeatureTile("Route map", "Read-only map preview — no routing yet.",
                Icons.AutoMirrored.Rounded.AltRoute, WagonColors.DashboardBlue,
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
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(WagonSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(WagonSpacing.lg)) {
        SectionHeader("Local record", "Progress", "Stored only on this device.")
        WagonCard(Modifier.fillMaxWidth()) {
            WagonProgress(
                if (progress.inspectionTotal == 0) 0f else progress.inspectionCompleted.toFloat() / progress.inspectionTotal,
                "Inspection items")
            Text(progress.inspectionCompleted.toString() + " of " + progress.inspectionTotal + " complete",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(WagonSpacing.md)) {
            Metric("PRACTICE", progress.testsCompleted.toString(), "attempts", Modifier.weight(1f))
            Metric("ACCURACY", progress.testAccuracyPercent.toString() + "%", "sample", Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(WagonSpacing.md)) {
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
    WagonCard(modifier.heightIn(min = 126.dp)) {
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
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(WagonSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(WagonSpacing.lg)) {
        SectionHeader("Cab setup", "Settings & about", "Tune readability without an account.")
        WagonCard(Modifier.fillMaxWidth()) {
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
            "lastwagon sample content is not official guidance and does not replace regulations, training, employer procedures, required inspections, or driver judgment.",
            Icons.Rounded.HealthAndSafety, WagonColors.MarkerAmber)
        WagonCard(Modifier.fillMaxWidth()) {
            Text("ROUTING & ADDRESS SEARCH", style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary)
            Text("OpenRouteService API key (optional)", style = MaterialTheme.typography.titleMedium)
            Text(
                "Route preview and address search normally use the app's built-in key. " +
                    "Paste your own free key from account.heigit.org to use your own request " +
                    "quota instead — helpful if the built-in key stops working. The key stays " +
                    "on this device only and is excluded from Android backups and transfers.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            // Re-keyed on the stored value so the field follows saves and clears.
            var keyText by rememberSaveable(prefs.orsApiKeyOverride) {
                mutableStateOf(prefs.orsApiKeyOverride)
            }
            // The key is a credential: masked by default, explicit reveal.
            var keyVisible by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                value = keyText,
                onValueChange = { keyText = it },
                label = { Text("Your API key") },
                singleLine = true,
                visualTransformation = if (keyVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { keyVisible = !keyVisible }) {
                        Icon(
                            if (keyVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = if (keyVisible) "Hide key" else "Show key",
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(WagonSpacing.sm)) {
                Button(
                    // Saving a cleared field is a valid way to drop the custom key.
                    onClick = { scope.launch { preferencesRepository.setOrsApiKeyOverride(keyText) } },
                    enabled = keyText.trim() != prefs.orsApiKeyOverride,
                ) { Text("Save key") }
                TextButton(
                    onClick = { scope.launch { preferencesRepository.setOrsApiKeyOverride("") } },
                    enabled = prefs.orsApiKeyOverride.isNotEmpty(),
                ) { Text("Use built-in key") }
            }
            if (prefs.orsApiKeyOverride.isNotEmpty()) {
                Text("Using your key for routing and address search.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary)
            }
        }
        WagonCard(Modifier.fillMaxWidth()) {
            Text("ABOUT", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            Text("lastwagon " + appVersion, style = MaterialTheme.typography.titleLarge)
            Text("Native Kotlin • Jetpack Compose • Offline first", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Open-source acknowledgments", style = MaterialTheme.typography.titleMedium)
            Text("A complete dependency and license inventory will be finalized before production release.",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable private fun SettingSwitch(title: String, subtitle: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 64.dp), horizontalArrangement = Arrangement.spacedBy(WagonSpacing.sm)) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked, onChecked)
    }
}
