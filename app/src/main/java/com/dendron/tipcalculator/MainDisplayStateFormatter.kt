package com.dendron.tipcalculator

import java.text.NumberFormat

class MainDisplayStateFormatter(
    private val currencyFormat: NumberFormat,
) {
    fun format(
        state: MainUiState,
        minSplitCount: Int,
        maxSplitCount: Int,
    ): MainDisplayState {
        return MainDisplayState(
            billInput = state.billInput,
            tipPercent = state.tipPercent,
            splitNum = state.splitNum,
            roundUp = state.roundUp,
            totalToPayText = currencyFormat.format(state.result.totalToPay),
            totalPerPersonText = currencyFormat.format(state.result.totalPerPerson),
            totalTipText = currencyFormat.format(state.result.totalTip),
            tipPerPersonText = currencyFormat.format(state.result.tipPerPerson),
            tipPercentText = "${state.tipPercent} %",
            isSplitDecreaseEnabled = state.splitNum > minSplitCount,
            isSplitIncreaseEnabled = state.splitNum < maxSplitCount,
        )
    }
}
