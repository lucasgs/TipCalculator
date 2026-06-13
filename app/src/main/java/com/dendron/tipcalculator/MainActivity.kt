package com.dendron.tipcalculator

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.dendron.tipcalculator.databinding.ActivityMainBinding
import com.dendron.tipcalculator.domain.Result
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpViewModel()
        setUpViews()
        initializeDefaults()
    }

    private fun setUpViewModel() {
        viewModel.getResults().observe(this, { result ->
            showNumbers(result)
        })
    }

    private fun setUpViews() {

        binding.apply {

            edtBillTotal.addTextChangedListener {
                val rawValue = it?.toString().orEmpty()
                val normalizedValue = rawValue.replace(',', '.')

                when {
                    rawValue.isBlank() -> {
                        edtBillTotal.error = null
                        viewModel.setBillTotal(0.0)
                    }
                    normalizedValue.toDoubleOrNull() != null -> {
                        edtBillTotal.error = null
                        viewModel.setBillTotal(normalizedValue.toDouble())
                    }
                    else -> {
                        edtBillTotal.error = getString(R.string.invalid_bill_total_error)
                    }
                }
            }

            sbTipPercent.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    viewModel.setTipPercentage(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })

            sbSplit.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    viewModel.setSplitNumber(progress + 1)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })

            rgRounding.setOnCheckedChangeListener { _: RadioGroup, checkedId: Int ->
                viewModel.setIsRoundUp(checkedId == rbRoundUp.id)
            }
        }
    }

    private fun initializeDefaults() {
        binding.apply {
            sbTipPercent.progress = 10
            sbSplit.progress = 0
            rgRounding.check(rbRoundUp.id)
        }
    }

    private fun showNumbers(result: Result) {
        val defaultFormat: NumberFormat = NumberFormat.getCurrencyInstance()

        binding.apply {
            lblTotalToPayAmount.text = defaultFormat.format(result.totalToPay)
            lblTotalPerPersonAmount.text = defaultFormat.format(result.totalPerPerson)
            lblTotalTipAmount.text = defaultFormat.format(result.totalTip)
            lblTipPerPersonAmount.text = defaultFormat.format(result.tipPerPerson)
            lblTipPercentAmount.text = getString(R.string.tip_percentage_amount_title, result.tipPercent)
            lblSplitCount.text = result.splitNum.toString()
        }
    }
}