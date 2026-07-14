package com.trkrhlpr.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.trkrhlpr.core.designsystem.*
import com.trkrhlpr.core.model.*
import com.trkrhlpr.feature.dashboard.*
import com.trkrhlpr.feature.learning.*
import kotlinx.coroutines.launch

private object Routes {
    const val Home = "home"
    const val Progress = "progress"
    const val Settings = "settings"
    const val Inspection = "inspection"
    const val Practice = "practice"
    const val Daily = "daily"
}

private data class Destination(val route: String, val label: String, val icon: ImageVector)
private val topDestinations = listOf(
    Destination(Routes.Home, "Home", Icons.Rounded.Dashboard),
    Destination(Routes.Progress, "Progress", Icons.Rounded.QueryStats),
    Destination(Routes.Settings, "Settings", Icons.Rounded.Tune),
)

@Composable
fun TrkrHlprApp(container: AppContainer) {
    val preferences by container.preferencesRepository.preferences.collectAsState(initial = UserPreferences())
    val dark = when (preferences.theme) {
        ThemePreference.DARK -> true
        ThemePreference.LIGHT -> false
        ThemePreference.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
    }
    TrkrHlprTheme(
        darkTheme = dark,
        largeText = preferences.largeText,
        reduceMotion = preferences.reduceMotion,
    ) {
        val navController = rememberNavController()
        val backStack by navController.currentBackStackEntryAsState()
        val current = backStack?.destination
        val snackbar = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val useRail = maxWidth >= 700.dp
            Scaffold(
                snackbarHost = { SnackbarHost(snackbar) },
                bottomBar = {
                    if (!useRail && current?.route in topDestinations.map { it.route }) {
                        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                            topDestinations.forEach { destination ->
                                NavigationBarItem(
                                    selected = current?.hierarchy?.any { it.route == destination.route } == true,
                                    onClick = { navController.navigateTop(destination.route) },
                                    icon = { Icon(destination.icon, destination.label) },
                                    label = { Text(destination.label) },
                                )
                            }
                        }
                    }
                },
            ) { padding ->
                Row(Modifier.fillMaxSize().padding(padding)) {
                    if (useRail && current?.route in topDestinations.map { it.route }) {
                        NavigationRail(containerColor = MaterialTheme.colorScheme.surface) {
                            Spacer(Modifier.height(24.dp))
                            Icon(Icons.Rounded.LocalShipping, "trkrhlpr", tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.height(24.dp))
                            topDestinations.forEach { destination ->
                                NavigationRailItem(
                                    selected = current?.hierarchy?.any { it.route == destination.route } == true,
                                    onClick = { navController.navigateTop(destination.route) },
                                    icon = { Icon(destination.icon, destination.label) },
                                    label = { Text(destination.label) },
                                )
                            }
                        }
                    }
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Home,
                        modifier = Modifier.weight(1f),
                    ) {
                        composable(Routes.Home) {
                            HomeScreen(
                                onInspection = { navController.navigate(Routes.Inspection) },
                                onPractice = { navController.navigate(Routes.Practice) },
                                onDaily = { navController.navigate(Routes.Daily) },
                                onProgress = { navController.navigateTop(Routes.Progress) },
                            )
                        }
                        composable(Routes.Progress) {
                            ProgressScreen(container.progressRepository, onResetComplete = {
                                scope.launch { snackbar.showSnackbar("Local progress reset") }
                            })
                        }
                        composable(Routes.Settings) {
                            SettingsScreen(container.preferencesRepository, BuildConfig.VERSION_NAME)
                        }
                        composable(Routes.Inspection) {
                            ScreenWithBack("Pre-trip", { navController.popBackStack() }) {
                                InspectionScreen(container.contentRepository, container.progressRepository)
                            }
                        }
                        composable(Routes.Practice) {
                            ScreenWithBack("CDL practice", { navController.popBackStack() }) {
                                PracticeScreen(container.contentRepository, container.progressRepository)
                            }
                        }
                        composable(Routes.Daily) {
                            ScreenWithBack("Daily safety", { navController.popBackStack() }) {
                                DailyQuestionScreen(container.contentRepository, container.progressRepository)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenWithBack(title: String, onBack: () -> Unit, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back") }
                },
            )
        },
    ) { padding -> Box(Modifier.fillMaxSize().padding(padding)) { content() } }
}

private fun androidx.navigation.NavHostController.navigateTop(route: String) {
    navigate(route) {
        popUpTo(Routes.Home) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
