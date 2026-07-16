package com.lastwagon.app

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalTestApi::class)
class NavigationSmokeTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun homeOpensInspectionWorkspace() {
        compose.onNodeWithText("Pre-trip inspection").performClick()
        compose.onNodeWithText("Inspection workspace").assertIsDisplayed()
    }

    @Test fun dailyQuestionAcceptsAnAnswer() {
        compose.onNodeWithText("Daily safety").performClick()
        compose.waitUntilAtLeastOneExists(hasText("One deliberate minute"), 5_000)
        compose.onAllNodesWithText("Stop in a safe place and reassess")[0].performClick()
        compose.onNodeWithText("Submit answer").performClick()
        compose.onNodeWithText("Daily check recorded").assertIsDisplayed()
    }

    @Test fun routeMapOpensWithoutRequestingLocation() {
        compose.onNodeWithText("Route map").performScrollTo().performClick()
        compose.onNodeWithText("Vehicle profile").assertIsDisplayed()
        compose.onNodeWithText("Confirm and save profile").assertIsDisplayed()
    }
}
