package com.dendron.tipcalculator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dendron.tipcalculator.domain.Result
import com.dendron.tipcalculator.domain.TipCalculator
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: MainViewModel
    lateinit var tipCalculator: TipCalculator

    @Before
    fun setUp() {
        tipCalculator = TipCalculator()
        viewModel = MainViewModel(tipCalculator)
    }

    @Test
    fun give_a_total_should_return_valid_result() {

        val result = Result().apply {
            billTotal = 100.0
            tipPercent = 10
            splitNum = 1
            roundUp = true
            totalTip = 10.0
            tipPerPerson = 10.0
            totalPerPerson = 110.0
            totalToPay = 110.0
        }
        viewModel.setBillTotal(100.0)

        assertEquals(viewModel.getResults().value, result)

    }

    @Test
    fun give_a_total_and_split_number_should_return_valid_result2() {

        val result = Result().apply {
            billTotal = 100.0
            tipPercent = 10
            splitNum = 2
            roundUp = true
            totalTip = 10.0
            tipPerPerson = 5.0
            totalPerPerson = 55.0
            totalToPay = 110.0
        }
        viewModel.setBillTotal(100.0)
        viewModel.setSplitNumber(2)

        assertEquals(viewModel.getResults().value, result)
    }

    @Test
    fun give_a_total_and_split_number_and_tip_percentage_should_return_valid_result2() {

        val result = Result().apply {
            billTotal = 100.0
            tipPercent = 20
            splitNum = 2
            roundUp = true
            totalTip = 20.0
            tipPerPerson = 10.0
            totalPerPerson = 60.0
            totalToPay = 120.0
        }
        viewModel.setBillTotal(100.0)
        viewModel.setSplitNumber(2)
        viewModel.setTipPercentage(20)

        assertEquals(viewModel.getResults().value, result)
    }
}