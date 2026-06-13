package com.dendron.tipcalculator

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityStateRestoreTest {

    @Test
    fun recreating_activity_restores_ui_facing_control_state() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        scenario.onActivity { activity ->
            activity.applyStateForTest(
                billInput = "12.5",
                tipPercent = 17,
                splitNum = 3,
                roundUp = false,
            )
        }

        scenario.recreate()

        scenario.onActivity { activity ->
            val state = activity.currentDisplayStateForTest()
            assertEquals("12.5", state.billInput)
            assertTrue(state.isCustomTip)
            assertEquals(17, state.tipPercent)
            assertEquals("17 %", state.tipPercentText)
            assertEquals(3, state.splitNum)
            assertFalse(state.roundUp)
        }

        scenario.close()
    }
}
