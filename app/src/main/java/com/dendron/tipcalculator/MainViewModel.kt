package com.dendron.tipcalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val calculationResult = MutableLiveData<Result>()
    fun getResults(): LiveData<Result> = calculationResult

    private val tipCalculator = TipCalculator()

    private var billTotal: Double = 100.0
    private var tipPercent: Int = 10
    private var splitNum: Int = 2
    private var roundUp: Boolean = true

    fun setBillTotal(total: Double) {
        billTotal = total
        recalculate()
    }

    fun setTipPercentage(tip: Int) {
        tipPercent = tip
        recalculate()
    }

    fun setSplitNumber(number: Int) {
        splitNum = number
        recalculate()
    }

    fun setIsRoundUp(isRoundUp: Boolean) {
        roundUp = isRoundUp
        recalculate()
    }

    private fun recalculate() {

        val result = tipCalculator.calculate(
            billTotal = billTotal,
            tipPercent = tipPercent,
            splitNum = splitNum,
            roundUp = roundUp
        )
        calculationResult.postValue(result)
    }

}