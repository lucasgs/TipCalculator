package com.dendron.tipcalculator

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TipCalculatorScreen(
    state: MainDisplayState,
    billInputError: String?,
    onBillInputChanged: (String) -> Unit,
    onTipPresetSelected: (Int?) -> Unit,
    onTipCustomChanged: (Int) -> Unit,
    onSplitDecrease: () -> Unit,
    onSplitIncrease: () -> Unit,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.input_section_title),
                    style = MaterialTheme.typography.headlineSmall,
                )

                OutlinedTextField(
                    value = state.billInput,
                    onValueChange = onBillInputChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .testTag("billInput"),
                    label = { Text(stringResource(R.string.bill_total_hint)) },
                    isError = billInputError != null,
                    supportingText = {
                        if (billInputError != null) {
                            Text(billInputError)
                        }
                    },
                    prefix = { Text(stringResource(R.string.currency_prefix)) },
                    singleLine = true,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.tip_percentage_label),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(text = state.tipPercentText, style = MaterialTheme.typography.bodyLarge)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TipPresetChip(stringResource(R.string.tip_preset_10), state.tipPercent == 10, onClick = { onTipPresetSelected(10) })
                    TipPresetChip(stringResource(R.string.tip_preset_15), state.tipPercent == 15, onClick = { onTipPresetSelected(15) })
                    TipPresetChip(stringResource(R.string.tip_preset_18), state.tipPercent == 18, onClick = { onTipPresetSelected(18) })
                    TipPresetChip(stringResource(R.string.tip_preset_20), state.tipPercent == 20, onClick = { onTipPresetSelected(20) })
                    TipPresetChip(
                        stringResource(R.string.tip_preset_custom),
                        state.isCustomTip,
                        onClick = { onTipPresetSelected(null) },
                    )
                }

                if (state.isCustomTip) {
                    Slider(
                        value = state.tipPercent.toFloat(),
                        onValueChange = { onTipCustomChanged(it.toInt()) },
                        valueRange = 0f..100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .testTag("customTipSlider"),
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.split_label),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedButton(onClick = onSplitDecrease, enabled = state.isSplitDecreaseEnabled) {
                            Text(stringResource(R.string.split_decrease_label))
                        }
                        Text(
                            text = state.splitNum.toString(),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.testTag("splitCount"),
                        )
                        OutlinedButton(onClick = onSplitIncrease, enabled = state.isSplitIncreaseEnabled) {
                            Text(stringResource(R.string.split_increase_label))
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.rounding_label),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 20.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(
                        selected = state.roundUp,
                        onClick = { onRoundUpChanged(true) },
                        label = { Text(stringResource(R.string.round_up_label)) },
                        modifier = Modifier.testTag("roundUpChip"),
                    )
                    FilterChip(
                        selected = !state.roundUp,
                        onClick = { onRoundUpChanged(false) },
                        label = { Text(stringResource(R.string.round_down_label)) },
                        modifier = Modifier.testTag("roundDownChip"),
                    )
                }
            }
        }

        TipSummaryCard(state = state)
    }
}

@Composable
private fun TipPresetChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
    )
}
