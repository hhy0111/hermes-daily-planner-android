package com.hermes.studyvault.settings

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hermes.studyvault.MainActivity
import org.junit.Rule
import org.junit.Test

class LanguageSettingsInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun selectingLanguageUpdatesVisibleAppText() {
        composeRule.onNodeWithTag("settings_button").performClick()
        composeRule.onNodeWithText("Language").assertExists()

        composeRule.onNodeWithTag("language_ko").performClick()
        composeRule.onNodeWithTag("back_button").performClick()
        composeRule.onNodeWithText("오늘 계획").assertExists()

        composeRule.onNodeWithTag("settings_button").performClick()
        composeRule.onNodeWithTag("language_en").performClick()
        composeRule.onNodeWithTag("back_button").performClick()
        composeRule.onNodeWithText("Today Plan").assertExists()
    }
}
