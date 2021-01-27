package com.dendron.tipcalculator.domain

data class Result(
    var billTotal: Double = 0.0,
    var tipPercent: Int = 0,
    var splitNum: Int = 0,
    var roundUp: Boolean = true,
    var totalTip: Double = 0.0,
    var tipPerPerson: Double = 0.0,
    var totalPerPerson: Double = 0.0,
    var totalToPay: Double = 0.0,
)