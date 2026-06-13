package com.dendron.tipcalculator

data class MainDisplayState(
    val billInput: String,
    val tipPercent: Int,
    val splitNum: Int,
    val roundUp: Boolean,
    val totalToPayText: String,
    val totalPerPersonText: String,
    val totalTipText: String,
    val tipPerPersonText: String,
    val tipPercentText: String,
    val isSplitDecreaseEnabled: Boolean,
    val isSplitIncreaseEnabled: Boolean,
)
