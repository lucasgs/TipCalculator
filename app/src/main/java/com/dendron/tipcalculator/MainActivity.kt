package com.dendron.tipcalculator

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.dendron.tipcalculator.databinding.ActivityMainBinding
import com.dendron.tipcalculator.ui.theme.TipCalculatorComposeTheme
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private companion object {
        const val MIN_SPLIT_COUNT = 1
        const val MAX_SPLIT_COUNT = 20
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(extras.createSavedStateHandle()) as T
            }
        }
    }

    private val currentLocale: Locale by lazy { resources.configuration.locales[0] ?: Locale.getDefault() }
    private val decimalSeparator: Char by lazy { DecimalFormatSymbols.getInstance(currentLocale).decimalSeparator }
    private val groupingSeparator: Char by lazy { DecimalFormatSymbols.getInstance(currentLocale).groupingSeparator }
    private val displayStateFormatter: MainDisplayStateFormatter by lazy {
        MainDisplayStateFormatter(NumberFormat.getCurrencyInstance(currentLocale))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.composeRoot.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed,
        )

        binding.composeRoot.setContent {
            val uiState by viewModel.getUiState().observeAsState(MainUiState())
            val displayState = displayStateFormatter.format(uiState, MIN_SPLIT_COUNT, MAX_SPLIT_COUNT)
            val billInputError = uiState.billInput
                .takeIf { it.isNotBlank() && parseLocalizedAmount(it) == null }
                ?.let { getString(R.string.invalid_bill_total_error) }

            TipCalculatorComposeTheme {
                TipCalculatorScreen(
                    state = displayState,
                    billInputError = billInputError,
                    onBillInputChanged = viewModel::setBillInput,
                    onTipPresetSelected = { tip ->
                        viewModel.setIsCustomTip(tip == null)
                        if (tip != null) {
                            viewModel.setTipPercentage(tip)
                        }
                    },
                    onTipCustomChanged = {
                        viewModel.setIsCustomTip(true)
                        viewModel.setTipPercentage(it)
                    },
                    onSplitDecrease = {
                        if (displayState.isSplitDecreaseEnabled) {
                            viewModel.setSplitNumber(displayState.splitNum - 1)
                        }
                    },
                    onSplitIncrease = {
                        if (displayState.isSplitIncreaseEnabled) {
                            viewModel.setSplitNumber(displayState.splitNum + 1)
                        }
                    },
                    onRoundUpChanged = viewModel::setIsRoundUp,
                )
            }
        }
    }

    internal fun applyStateForTest(
        billInput: String,
        tipPercent: Int,
        splitNum: Int,
        roundUp: Boolean,
    ) {
        viewModel.setBillInput(billInput)
        viewModel.setIsCustomTip(tipPercent !in setOf(10, 15, 18, 20))
        viewModel.setTipPercentage(tipPercent)
        viewModel.setSplitNumber(splitNum)
        viewModel.setIsRoundUp(roundUp)
    }

    internal fun currentDisplayStateForTest(): MainDisplayState {
        val state = viewModel.getUiState().value ?: MainUiState()
        return displayStateFormatter.format(state, MIN_SPLIT_COUNT, MAX_SPLIT_COUNT)
    }

    private fun parseLocalizedAmount(rawValue: String): Double? {
        val sanitized = rawValue
            .replace(groupingSeparator.toString(), "")
            .replace(" ", "")
            .replace(decimalSeparator, '.')

        return sanitized.toDoubleOrNull()
    }
}
