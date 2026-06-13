package com.dendron.tipcalculator

import java.text.NumberFormat

class MainDisplayStateFormatter(
    private val currencyFormat: NumberFormat,
) {
    fun format(state: MainUiState): MainDisplayState {
        return MainDisplayState(
            billInput = state.billInput,
            tipPercent = state.tipPercent,
            isCustomTip = state.isCustomTip,
            splitNum = state.splitNum,
            roundUp = state.roundUp,
            totalToPayText = currencyFormat.format(state.result.totalToPay),
            totalPerPersonText = currencyFormat.format(state.result.totalPerPerson),
            totalTipText = currencyFormat.format(state.result.totalTip),
            tipPerPersonText = currencyFormat.format(state.result.tipPerPerson),
            tipPercentText = "${state.tipPercent} %",
            isSplitDecreaseEnabled = state.splitNum > TipCalculatorLimits.MIN_SPLIT_COUNT,
            isSplitIncreaseEnabled = state.splitNum < TipCalculatorLimits.MAX_SPLIT_COUNT,
        )
    }
}
