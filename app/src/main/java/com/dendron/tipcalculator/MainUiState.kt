package com.dendron.tipcalculator

import com.dendron.tipcalculator.domain.Result

data class MainUiState(
    val billInput: String = "",
    val billTotal: Double = 0.0,
    val tipPercent: Int = 10,
    val isCustomTip: Boolean = false,
    val splitNum: Int = 1,
    val roundUp: Boolean = true,
    val result: Result = Result(
        billTotal = 0.0,
        tipPercent = 10,
        splitNum = 1,
        roundUp = true,
        totalTip = 0.0,
        tipPerPerson = 0.0,
        totalPerPerson = 0.0,
        totalToPay = 0.0,
    ),
)
