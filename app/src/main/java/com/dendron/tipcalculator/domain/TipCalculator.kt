package com.dendron.tipcalculator.domain

import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.floor

class TipCalculator @Inject constructor() {

    fun calculate(
        billTotal: Double,
        tipPercent: Int,
        splitNum: Int,
        roundUp: Boolean
    ): Result {
        return Result().apply {
            totalTip = 0.01 * tipPercent * billTotal
            totalToPay = billTotal + totalTip
            if (roundUp) {
                totalToPay = ceil(totalToPay)
            } else {
                if (floor(totalToPay) >= totalToPay) {
                    totalToPay = floor(totalToPay)
                }
            }
            if (splitNum > 0) {
                totalPerPerson = totalToPay / splitNum
                tipPerPerson = totalTip / splitNum
            }

            this.billTotal = billTotal
            this.tipPercent = tipPercent
            this.splitNum = splitNum
            this.roundUp = roundUp
        }
    }
}