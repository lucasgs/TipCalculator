package com.dendron.tipcalculator

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TipCalculatorScreen(
    viewModel: MainViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currentLocale = remember { context.resources.configuration.locales[0] ?: Locale.getDefault() }
    val decimalSeparator = remember { DecimalFormatSymbols.getInstance(currentLocale).decimalSeparator }
    val groupingSeparator = remember { DecimalFormatSymbols.getInstance(currentLocale).groupingSeparator }
    val currencyFormat = remember(currentLocale) { NumberFormat.getCurrencyInstance(currentLocale) }
    val currencySymbol = remember(currencyFormat, currentLocale) {
        currencyFormat.currency?.getSymbol(currentLocale).orEmpty()
    }
    val formatter = remember(currencyFormat) { MainDisplayStateFormatter(currencyFormat) }

    val uiState by viewModel.getUiState().observeAsState(MainUiState())
    val displayState = remember(uiState, formatter) { formatter.format(uiState) }
    val billInputError = uiState.billInput
        .takeIf { it.isNotBlank() && parseLocalizedAmount(it, decimalSeparator, groupingSeparator) == null }
        ?.let { stringResource(R.string.invalid_bill_total_error) }

    TipCalculatorScreenContent(
        state = displayState,
        billInputError = billInputError,
        currencySymbol = currencySymbol,
        onBillInputChanged = viewModel::setBillInput,
        onTipPresetSelected = { tip ->
            viewModel.setIsCustomTip(tip == null)
            if (tip != null) {
                viewModel.setTipPercentage(tip)
            }
        },
        onTipCustomChanged = {
            viewModel.setIsCustomTip(true)
            viewModel.setTipPercentage(it)
        },
        onSplitDecrease = {
            if (displayState.isSplitDecreaseEnabled) {
                viewModel.setSplitNumber(displayState.splitNum - 1)
            }
        },
        onSplitIncrease = {
            if (displayState.isSplitIncreaseEnabled) {
                viewModel.setSplitNumber(displayState.splitNum + 1)
            }
        },
        onRoundUpChanged = viewModel::setIsRoundUp,
        modifier = modifier,
    )
}

@Composable
private fun TipCalculatorScreenContent(
    state: MainDisplayState,
    billInputError: String?,
    currencySymbol: String,
    onBillInputChanged: (String) -> Unit,
    onTipPresetSelected: (Int?) -> Unit,
    onTipCustomChanged: (Int) -> Unit,
    onSplitDecrease: () -> Unit,
    onSplitIncrease: () -> Unit,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val tipPresetsGroupDescription = stringResource(R.string.tip_presets_group_description)
    val customTipSliderDescription = stringResource(R.string.custom_tip_slider_description)
    val splitDecreaseDescription = stringResource(R.string.split_decrease_content_description)
    val splitIncreaseDescription = stringResource(R.string.split_increase_content_description)
    val splitCountDescription = pluralStringResource(
        R.plurals.split_count_content_description,
        state.splitNum,
        state.splitNum,
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
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
                        modifier = Modifier.semantics { heading() },
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
                                Text(
                                    text = billInputError,
                                    modifier = Modifier.semantics {
                                        liveRegion = LiveRegionMode.Assertive
                                    },
                                )
                            }
                        },
                        prefix = if (currencySymbol.isBlank()) null else ({ Text(currencySymbol) }),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
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
                            .horizontalScroll(rememberScrollState())
                            .selectableGroup()
                            .semantics {
                                contentDescription = tipPresetsGroupDescription
                            },
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
                                .testTag("customTipSlider")
                                .semantics {
                                    contentDescription = customTipSliderDescription
                                    stateDescription = state.tipPercentText
                                },
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
                            OutlinedButton(
                                onClick = onSplitDecrease,
                                enabled = state.isSplitDecreaseEnabled,
                                modifier = Modifier.semantics {
                                    contentDescription = splitDecreaseDescription
                                },
                            ) {
                                Text(stringResource(R.string.split_decrease_label))
                            }
                            Text(
                                text = state.splitNum.toString(),
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .testTag("splitCount")
                                    .semantics {
                                        contentDescription = splitCountDescription
                                    },
                            )
                            OutlinedButton(
                                onClick = onSplitIncrease,
                                enabled = state.isSplitIncreaseEnabled,
                                modifier = Modifier.semantics {
                                    contentDescription = splitIncreaseDescription
                                },
                            ) {
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
                            .padding(top = 8.dp)
                            .selectableGroup(),
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
}

@Composable
private fun TipPresetChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val selectedState = stringResource(
        if (selected) R.string.selected_state else R.string.not_selected_state,
    )
    val stateText = stringResource(R.string.tip_preset_state_description, text, selectedState)

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        modifier = Modifier.semantics {
            stateDescription = stateText
        },
    )
}

private fun parseLocalizedAmount(
    rawValue: String,
    decimalSeparator: Char,
    groupingSeparator: Char,
): Double? {
    val sanitized = rawValue
        .replace(groupingSeparator.toString(), "")
        .replace(" ", "")
        .replace(decimalSeparator, '.')

    return sanitized.toDoubleOrNull()
}
