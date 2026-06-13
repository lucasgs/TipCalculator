package com.dendron.tipcalculator

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TipCalculatorComposeUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun invalid_bill_input_shows_error_message() {
        composeRule.onNodeWithTag("billInput").performTextInput("12.5.")

        composeRule.onNodeWithText("Enter a valid amount").assertIsDisplayed()
    }

    @Test
    fun reset_restores_default_ui_state() {
        composeRule.onNodeWithTag("billInput").performTextInput("100")
        composeRule.onNodeWithText("Custom").performClick()
        composeRule.onNodeWithTag("customTipSlider").assertIsDisplayed()
        composeRule.onNodeWithTag("splitCount").assertTextEquals("1")
        composeRule.onNodeWithTag("splitIncreaseButton").performClick()
        composeRule.onNodeWithTag("splitCount").assertTextEquals("2")
        composeRule.onNodeWithTag("roundDownChip").performClick()
        composeRule.onNodeWithTag("roundDownChip").assertIsSelected()

        composeRule.onNodeWithTag("resetButton").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("10%").assertIsSelected()
        composeRule.onNodeWithText("Custom").assertIsNotSelected()
        composeRule.onNodeWithTag("splitCount").assertTextEquals("1")
        composeRule.onNodeWithTag("roundUpChip").assertIsSelected()
        composeRule.onNodeWithTag("roundDownChip").assertIsNotSelected()
        composeRule.onAllNodesWithTag("customTipSlider").assertCountEquals(0)
    }

    @Test
    fun summary_actions_are_visible() {
        composeRule.onNodeWithTag("resetButton").assertIsDisplayed()
        composeRule.onNodeWithTag("shareButton").assertIsDisplayed()
        composeRule.onNodeWithTag("totalPerPersonValue").assertIsDisplayed()
    }

    @Test
    fun replacing_invalid_input_with_valid_value_clears_error() {
        composeRule.onNodeWithTag("billInput").performTextInput("12.5.")
        composeRule.onNodeWithText("Enter a valid amount").assertIsDisplayed()

        composeRule.onNodeWithTag("billInput").performTextReplacement("12.5")
        composeRule.waitForIdle()

        composeRule.onAllNodesWithText("Enter a valid amount").assertCountEquals(0)
    }
}
