package com.dendron.tipcalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dendron.tipcalculator.domain.Result
import com.dendron.tipcalculator.domain.TipCalculator

class MainViewModel(private val tipCalculator: TipCalculator = TipCalculator()) : ViewModel() {

    private val calculationResult = MutableLiveData<Result>()
    fun getResults(): LiveData<Result> = calculationResult

    private var billTotal: Double = 0.0
    private var tipPercent: Int = 10
    private var splitNum: Int = 1
    private var roundUp: Boolean = true

    init {
        recalculate()
    }

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