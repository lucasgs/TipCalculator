package com.dendron.tipcalculator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
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

    private lateinit var viewModel: MainViewModel
    private lateinit var tipCalculator: TipCalculator

    @Before
    fun setUp() {
        tipCalculator = TipCalculator()
        viewModel = MainViewModel(SavedStateHandle(), tipCalculator)
    }

    @Test
    fun give_a_total_should_return_valid_result() {
        val result = Result(
            billTotal = 100.0,
            tipPercent = 10,
            splitNum = 1,
            roundUp = true,
            totalTip = 10.0,
            tipPerPerson = 10.0,
            totalPerPerson = 110.0,
            totalToPay = 110.0,
        )

        viewModel.setBillInput("100")

        assertEquals(result, viewModel.getUiState().value?.result)
        assertEquals("100", viewModel.getUiState().value?.billInput)
    }

    @Test
    fun give_a_total_and_split_number_should_return_valid_result() {
        val result = Result(
            billTotal = 100.0,
            tipPercent = 10,
            splitNum = 2,
            roundUp = true,
            totalTip = 10.0,
            tipPerPerson = 5.0,
            totalPerPerson = 55.0,
            totalToPay = 110.0,
        )

        viewModel.setBillInput("100")
        viewModel.setSplitNumber(2)

        assertEquals(result, viewModel.getUiState().value?.result)
    }

    @Test
    fun give_a_total_and_split_number_and_tip_percentage_should_return_valid_result() {
        val result = Result(
            billTotal = 100.0,
            tipPercent = 20,
            splitNum = 2,
            roundUp = true,
            totalTip = 20.0,
            tipPerPerson = 10.0,
            totalPerPerson = 60.0,
            totalToPay = 120.0,
        )

        viewModel.setBillInput("100")
        viewModel.setSplitNumber(2)
        viewModel.setTipPercentage(20)

        assertEquals(result, viewModel.getUiState().value?.result)
    }

    @Test
    fun restores_state_from_saved_state_handle() {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "billInput" to "50.00",
                "billTotal" to 50.0,
                "tipPercent" to 15,
                "splitNum" to 2,
                "roundUp" to false,
            ),
        )

        val restoredViewModel = MainViewModel(savedStateHandle, tipCalculator)
        val state = restoredViewModel.getUiState().value!!

        assertEquals("50.00", state.billInput)
        assertEquals(50.0, state.billTotal, 0.0)
        assertEquals(15, state.tipPercent)
        assertEquals(2, state.splitNum)
        assertEquals(false, state.roundUp)
        assertEquals(57.5, state.result.totalToPay, 0.0)
    }
}
