package com.dendron.tipcalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.dendron.tipcalculator.domain.TipCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tipCalculator: TipCalculator,
) : ViewModel() {

    private companion object {
        const val KEY_BILL_INPUT = "billInput"
        const val KEY_BILL_TOTAL = "billTotal"
        const val KEY_TIP_PERCENT = "tipPercent"
        const val KEY_IS_CUSTOM_TIP = "isCustomTip"
        const val KEY_SPLIT_NUM = "splitNum"
        const val KEY_ROUND_UP = "roundUp"
        const val DEFAULT_TIP_PERCENT = 10
        const val DEFAULT_SPLIT_NUM = 1
        const val DEFAULT_ROUND_UP = true
        val SUPPORTED_DECIMAL_SEPARATORS = charArrayOf(',', '.')
    }

    private val uiState = MutableLiveData(createUiState())

    fun getUiState(): LiveData<MainUiState> = uiState

    fun setBillInput(input: String) {
        savedStateHandle[KEY_BILL_INPUT] = input

        val normalizedValue = normalizeBillInput(input)
        when {
            input.isBlank() -> savedStateHandle[KEY_BILL_TOTAL] = 0.0
            normalizedValue.toDoubleOrNull() != null -> {
                savedStateHandle[KEY_BILL_TOTAL] = normalizedValue.toDouble()
            }
        }

        recalculate()
    }

    fun setBillTotal(total: Double) {
        savedStateHandle[KEY_BILL_TOTAL] = total
        savedStateHandle[KEY_BILL_INPUT] = if (total == 0.0) "" else total.toString()
        recalculate()
    }

    fun setTipPercentage(tip: Int) {
        savedStateHandle[KEY_TIP_PERCENT] = tip
        recalculate()
    }

    fun setIsCustomTip(isCustomTip: Boolean) {
        savedStateHandle[KEY_IS_CUSTOM_TIP] = isCustomTip
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
        val isCustomTip = savedStateHandle[KEY_IS_CUSTOM_TIP] ?: false
        val splitNum = savedStateHandle[KEY_SPLIT_NUM] ?: DEFAULT_SPLIT_NUM
        val roundUp = savedStateHandle[KEY_ROUND_UP] ?: DEFAULT_ROUND_UP
        val billInput = savedStateHandle[KEY_BILL_INPUT] ?: if (billTotal == 0.0) "" else billTotal.toString()

        return MainUiState(
            billInput = billInput,
            billTotal = billTotal,
            tipPercent = tipPercent,
            isCustomTip = isCustomTip,
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

    private fun normalizeBillInput(input: String): String {
        return buildString(input.length) {
            input.forEach { char ->
                when {
                    char.isWhitespace() -> Unit
                    char in SUPPORTED_DECIMAL_SEPARATORS -> append('.')
                    char.isDigit() -> append(char)
                }
            }
        }
    }
}
