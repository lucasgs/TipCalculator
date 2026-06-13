package com.dendron.tipcalculator

import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityStateRestoreTest {

    @Test
    fun recreating_activity_restores_ui_facing_control_state() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        scenario.onActivity { activity ->
            activity.findViewById<EditText>(R.id.edtBillTotal).setText("12.5")
            activity.findViewById<Chip>(R.id.chipTipCustom).performClick()
            activity.findViewById<SeekBar>(R.id.sbTipPercent).progress = 17
            activity.findViewById<android.view.View>(R.id.btnSplitIncrease).performClick()
            activity.findViewById<android.view.View>(R.id.btnSplitIncrease).performClick()
            activity.findViewById<android.view.View>(R.id.btnRoundDown).performClick()
        }

        scenario.recreate()

        scenario.onActivity { activity ->
            assertEquals("12.5", activity.findViewById<EditText>(R.id.edtBillTotal).text.toString())
            assertTrue(activity.findViewById<Chip>(R.id.chipTipCustom).isChecked)
            assertTrue(activity.findViewById<SeekBar>(R.id.sbTipPercent).isShown)
            assertEquals(17, activity.findViewById<SeekBar>(R.id.sbTipPercent).progress)
            assertEquals("3", activity.findViewById<TextView>(R.id.lblSplitCount).text.toString())
            assertEquals(
                R.id.btnRoundDown,
                activity.findViewById<MaterialButtonToggleGroup>(R.id.tgRounding).checkedButtonId,
            )
        }

        scenario.close()
    }
}
