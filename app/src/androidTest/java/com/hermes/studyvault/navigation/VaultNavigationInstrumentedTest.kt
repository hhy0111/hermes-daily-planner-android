package com.hermes.studyvault.navigation

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
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
        composeRule.onNodeWithText("Today").assertExists()
        composeRule.onNodeWithText("Inbox").performClick()
        composeRule.onNodeWithText("Capture Inbox").assertExists()
        composeRule.onNodeWithText("Vault").performClick()
        composeRule.onNodeWithText("Knowledge Vault").assertExists()
        composeRule.onNodeWithText("Write").performClick()
        composeRule.onNodeWithText("Write Note").assertExists()
        composeRule.onNodeWithText("Review").performClick()
        composeRule.onNodeWithText("Review Queue").assertExists()
    }
}
