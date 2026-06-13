package com.dendron.tipcalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.dendron.tipcalculator.domain.TipCalculator

class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val tipCalculator: TipCalculator = TipCalculator(),
) : ViewModel() {

    private companion object {
        const val KEY_BILL_TOTAL = "billTotal"
        const val KEY_TIP_PERCENT = "tipPercent"
        const val KEY_SPLIT_NUM = "splitNum"
        const val KEY_ROUND_UP = "roundUp"
        const val DEFAULT_TIP_PERCENT = 10
        const val DEFAULT_SPLIT_NUM = 1
        const val DEFAULT_ROUND_UP = true
    }

    private val uiState = MutableLiveData(createUiState())

    fun getUiState(): LiveData<MainUiState> = uiState

    fun setBillTotal(total: Double) {
        savedStateHandle[KEY_BILL_TOTAL] = total
        recalculate()
    }

    fun setTipPercentage(tip: Int) {
        savedStateHandle[KEY_TIP_PERCENT] = tip
        recalculate()
    }

    fun setSplitNumber(number: Int) {
        savedStateHandle[KEY_SPLIT_NUM] = number
        recalculate()
    }

    fun setIsRoundUp(isRoundUp: Boolean) {
        savedStateHandle[KEY_ROUND_UP] = isRoundUp
        recalculate()
    }

    private fun recalculate() {
        uiState.value = createUiState()
    }

    private fun createUiState(): MainUiState {
        val billTotal = savedStateHandle[KEY_BILL_TOTAL] ?: 0.0
        val tipPercent = savedStateHandle[KEY_TIP_PERCENT] ?: DEFAULT_TIP_PERCENT
        val splitNum = savedStateHandle[KEY_SPLIT_NUM] ?: DEFAULT_SPLIT_NUM
        val roundUp = savedStateHandle[KEY_ROUND_UP] ?: DEFAULT_ROUND_UP

        return MainUiState(
            billTotal = billTotal,
            tipPercent = tipPercent,
            splitNum = splitNum,
            roundUp = roundUp,
            result = tipCalculator.calculate(
                billTotal = billTotal,
                tipPercent = tipPercent,
                splitNum = splitNum,
                roundUp = roundUp,
            ),
        )
    }
}
