package com.dendron.tipcalculator

import com.dendron.tipcalculator.domain.Result
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.NumberFormat
import java.util.Locale

class MainDisplayStateFormatterTest {

    private val formatter = MainDisplayStateFormatter(NumberFormat.getCurrencyInstance(Locale.US))

    @Test
    fun formats_result_values_and_button_state() {
        val state = MainUiState(
            billInput = "12.5",
            tipPercent = 17,
            isCustomTip = true,
            splitNum = 3,
            roundUp = false,
            result = Result(
                billTotal = 12.5,
                tipPercent = 17,
                splitNum = 3,
                roundUp = false,
                totalTip = 2.125,
                tipPerPerson = 0.708333333,
                totalPerPerson = 4.875,
                totalToPay = 14.625,
            ),
        )

        val displayState = formatter.format(state, minSplitCount = 1, maxSplitCount = 20)

        assertEquals("12.5", displayState.billInput)
        assertEquals("$14.62", displayState.totalToPayText)
        assertEquals("$4.88", displayState.totalPerPersonText)
        assertEquals("$2.12", displayState.totalTipText)
        assertEquals("$0.71", displayState.tipPerPersonText)
        assertEquals("17 %", displayState.tipPercentText)
        assertTrue(displayState.isCustomTip)
        assertTrue(displayState.isSplitDecreaseEnabled)
        assertTrue(displayState.isSplitIncreaseEnabled)
        assertFalse(displayState.roundUp)
    }
}
