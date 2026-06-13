package com.dendron.tipcalculator

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.dendron.tipcalculator.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.ChipGroup
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private companion object {
        const val MIN_SPLIT_COUNT = 1
        const val MAX_SPLIT_COUNT = 20
        const val DEFAULT_TIP_PERCENT = 10
    }

    private var isSyncingUi = false

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
    private val defaultFormat: NumberFormat by lazy { NumberFormat.getCurrencyInstance(currentLocale) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpViewModel()
        setUpViews()
        initializeDefaults()
    }

    private fun setUpViewModel() {
        viewModel.getUiState().observe(this) { state ->
            showUiState(state)
        }
    }

    private fun setUpViews() {

        binding.apply {

            edtBillTotal.addTextChangedListener {
                if (isSyncingUi) return@addTextChangedListener

                val rawValue = it?.toString().orEmpty()
                val parsedValue = parseLocalizedAmount(rawValue)

                when {
                    rawValue.isBlank() -> {
                        tilBillTotal.error = null
                        viewModel.setBillInput(rawValue)
                    }
                    parsedValue != null -> {
                        tilBillTotal.error = null
                        viewModel.setBillInput(rawValue)
                    }
                    else -> {
                        tilBillTotal.error = getString(R.string.invalid_bill_total_error)
                        viewModel.setBillInput(rawValue)
                    }
                }
            }

            cgTipPresets.setOnCheckedStateChangeListener { _: ChipGroup, checkedIds: List<Int> ->
                if (isSyncingUi) return@setOnCheckedStateChangeListener

                when (checkedIds.firstOrNull()) {
                    chipTip10.id -> applyTipPreset(10)
                    chipTip15.id -> applyTipPreset(15)
                    chipTip18.id -> applyTipPreset(18)
                    chipTip20.id -> applyTipPreset(20)
                    chipTipCustom.id -> sbTipPercent.isVisible = true
                }
            }

            sbTipPercent.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (isSyncingUi) return

                    if (fromUser) {
                        cgTipPresets.check(chipTipCustom.id)
                    }
                    viewModel.setTipPercentage(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            btnSplitDecrease.setOnClickListener {
                val current = lblSplitCount.text.toString().toIntOrNull() ?: MIN_SPLIT_COUNT
                if (current > MIN_SPLIT_COUNT) {
                    viewModel.setSplitNumber(current - 1)
                }
            }

            btnSplitIncrease.setOnClickListener {
                val current = lblSplitCount.text.toString().toIntOrNull() ?: MIN_SPLIT_COUNT
                if (current < MAX_SPLIT_COUNT) {
                    viewModel.setSplitNumber(current + 1)
                }
            }

            tgRounding.addOnButtonCheckedListener { _: MaterialButtonToggleGroup, checkedId: Int, isChecked: Boolean ->
                if (isSyncingUi) return@addOnButtonCheckedListener

                if (isChecked) {
                    viewModel.setIsRoundUp(checkedId == btnRoundUp.id)
                }
            }
        }
    }

    private fun initializeDefaults() {
        showUiState(viewModel.getUiState().value ?: MainUiState())
    }

    private fun applyTipPreset(percent: Int) {
        binding.sbTipPercent.progress = percent
        binding.sbTipPercent.isVisible = false
        viewModel.setTipPercentage(percent)
    }

    private fun showUiState(state: MainUiState) {
        binding.apply {
            isSyncingUi = true

            syncBillTotal(state.billInput)
            syncTipControls(state.tipPercent)
            tgRounding.check(if (state.roundUp) btnRoundUp.id else btnRoundDown.id)

            lblTotalToPayAmount.text = defaultFormat.format(state.result.totalToPay)
            lblTotalPerPersonAmount.text = defaultFormat.format(state.result.totalPerPerson)
            lblTotalTipAmount.text = defaultFormat.format(state.result.totalTip)
            lblTipPerPersonAmount.text = defaultFormat.format(state.result.tipPerPerson)
            lblTipPercentAmount.text = getString(R.string.tip_percentage_amount_title, state.tipPercent)
            lblSplitCount.text = state.splitNum.toString()
            btnSplitDecrease.isEnabled = state.splitNum > MIN_SPLIT_COUNT
            btnSplitIncrease.isEnabled = state.splitNum < MAX_SPLIT_COUNT

            isSyncingUi = false
        }
    }

    private fun syncBillTotal(billInput: String) {
        if (binding.edtBillTotal.text?.toString() != billInput) {
            binding.edtBillTotal.setText(billInput)
            binding.edtBillTotal.setSelection(billInput.length)
        }
    }

    private fun syncTipControls(tipPercent: Int) {
        binding.sbTipPercent.progress = tipPercent

        when (tipPercent) {
            10 -> {
                binding.cgTipPresets.check(binding.chipTip10.id)
                binding.sbTipPercent.isVisible = false
            }
            15 -> {
                binding.cgTipPresets.check(binding.chipTip15.id)
                binding.sbTipPercent.isVisible = false
            }
            18 -> {
                binding.cgTipPresets.check(binding.chipTip18.id)
                binding.sbTipPercent.isVisible = false
            }
            20 -> {
                binding.cgTipPresets.check(binding.chipTip20.id)
                binding.sbTipPercent.isVisible = false
            }
            else -> {
                binding.cgTipPresets.check(binding.chipTipCustom.id)
                binding.sbTipPercent.isVisible = true
            }
        }
    }

    private fun parseLocalizedAmount(rawValue: String): Double? {
        val sanitized = rawValue
            .replace(groupingSeparator.toString(), "")
            .replace(" ", "")
            .replace(decimalSeparator, '.')

        return sanitized.toDoubleOrNull()
    }
}