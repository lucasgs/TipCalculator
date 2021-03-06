package com.dendron.tipcalculator

import android.os.Bundle
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
    }

    private fun setUpViewModel() {
        viewModel.getResults().observe(this, { result ->
            showNumbers(result)
        })
    }

    private fun setUpViews() {

        binding.apply {

            edtBillTotal.addTextChangedListener {
                val value = it?.toString()

                if (value?.isNotEmpty() == true) {
                    val billTotal = value.toDouble()
                    viewModel.setBillTotal(billTotal)
                }
            }

            //sbTipPercent.progress = 10
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

            //sbSplit.progress = 1
            sbSplit.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    viewModel.setSplitNumber(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })

            btnRoundUp.setOnClickListener {
                viewModel.setIsRoundUp(true)
            }

            btnRoundDown.setOnClickListener {
                viewModel.setIsRoundUp(false)
            }
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