package com.dendron.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.dendron.tipcalculator.ui.theme.TipCalculatorComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val currentLocale: Locale by lazy { resources.configuration.locales[0] ?: Locale.getDefault() }
    private val displayStateFormatter: MainDisplayStateFormatter by lazy {
        MainDisplayStateFormatter(NumberFormat.getCurrencyInstance(currentLocale))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TipCalculatorComposeTheme {
                TipCalculatorScreen()
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
        return displayStateFormatter.format(state)
    }
}
