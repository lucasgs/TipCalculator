package com.dendron.tipcalculator

import com.dendron.tipcalculator.domain.TipCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class TipCalculatorTest {

    lateinit var tipCalculator: TipCalculator

    @Before
    fun setUp() {
        tipCalculator = TipCalculator()
    }

    @Test
    fun `Given input data should return it in the result object`() {

        val result = tipCalculator.calculate(
            billTotal = 100.0,
            tipPercent = 10,
            splitNum = 1,
            roundUp = true
        )

        assertNotNull(result)
        assert(result.billTotal == 100.0)
        assert(result.tipPercent == 10)
        assert(result.splitNum == 1)
        assert(result.roundUp)
    }

    @Test
    fun `Given a total of 100, a percentage of 10, 1 person should return a total of 110`() {

        val result = tipCalculator.calculate(
            billTotal = 100.0,
            tipPercent = 10,
            splitNum = 1,
            roundUp = true
        )

        assert(result.totalToPay == 110.0)
        assert(result.totalPerPerson == 110.0)
        assert(result.totalTip == 10.0)
        assert(result.tipPerPerson == 10.0)
    }

    @Test
    fun `Given a total of 200, a percentage of 10, divided among 2, should return a total of 220`() {

        val result = tipCalculator.calculate(
            billTotal = 200.0,
            tipPercent = 10,
            splitNum = 2,
            roundUp = true
        )

        assert(result.totalToPay == 220.0)
        assert(result.totalPerPerson == 110.0)
        assert(result.totalTip == 20.0)
        assert(result.tipPerPerson == 10.0)
    }
    @Test
    fun `Given a total of 200, a percentage of 10, divided among 3, should return a total of 220`() {

        val result = tipCalculator.calculate(
            billTotal = 200.0,
            tipPercent = 10,
            splitNum = 3,
            roundUp = true
        )

        assert(result.totalToPay == 220.0)
        assertEquals(result.totalPerPerson, 73.333, 0.001)
        assert(result.totalTip == 20.0)
        assertEquals(result.tipPerPerson, 6.666, 0.001)
    }

    @Test
    fun `Given a total of 123_34, a percentage of 10, should return a total of 110`() {

        val result = tipCalculator.calculate(
            billTotal = 123.34,
            tipPercent = 10,
            splitNum = 1,
            roundUp = true
        )

        assert(result.totalToPay == 136.0)
        assert(result.totalPerPerson == result.totalToPay)
        assertEquals(result.totalTip, 12.334,  0.001)
        assert(result.tipPerPerson == result.totalTip)
    }

    @Test
    fun `Given a total of 123_34, a percentage of 10, divided among 3, should return a total of 135_674`() {

        val result = tipCalculator.calculate(
            billTotal = 123.34,
            tipPercent = 10,
            splitNum = 3,
            roundUp = true
        )

        assertEquals(result.totalToPay, 136.0, 0.001)
        assertEquals(result.totalPerPerson, 45.333, 0.001)
        assertEquals(result.totalTip, 12.334,  0.001)
        assertEquals(result.tipPerPerson, 4.111, 0.001)
    }

    @Test
    fun `Given a total of 123_34, a percentage of 10, divided among 3, no round up, should return a total of 135_674`() {

        val result = tipCalculator.calculate(
            billTotal = 123.34,
            tipPercent = 10,
            splitNum = 3,
            roundUp = false
        )

        assertEquals(result.totalToPay, 135.674, 0.001)
        assertEquals(result.totalPerPerson, 45.224, 0.001)
        assertEquals(result.totalTip, 12.334,  0.001)
        assertEquals(result.tipPerPerson, 4.111, 0.001)
    }
}