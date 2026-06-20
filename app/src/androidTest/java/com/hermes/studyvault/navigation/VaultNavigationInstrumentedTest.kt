package com.hermes.studyvault.navigation

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hermes.studyvault.MainActivity
import org.junit.Rule
import org.junit.Test

class VaultNavigationInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun bottomTabsNavigateBetweenMainScreens() {
        composeRule.onNodeWithTag("settings_button").performClick()
        composeRule.onNodeWithTag("language_en").performClick()
        composeRule.onNodeWithTag("back_button").performClick()

        composeRule.onNodeWithText("Today").assertExists()
        composeRule.onNodeWithText("Calendar").performClick()
        composeRule.onNodeWithText("Add schedules with a time and reminder.").assertExists()
        composeRule.onNodeWithText("Task List").performClick()
        composeRule.onNodeWithText("Add tasks, filter priorities, and keep attachments together.").assertExists()
        composeRule.onNodeWithText("Notes").performClick()
        composeRule.onNodeWithText("Keep quick notes connected to tasks and schedules.").assertExists()
        composeRule.onNodeWithText("Done").performClick()
        composeRule.onNodeWithText("Completed tasks appear here so you can reopen or reference them.").assertExists()
    }
}
