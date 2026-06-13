package com.dendron.tipcalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dendron.tipcalculator.ui.theme.TipCalculatorComposeTheme

@Composable
fun TipSummaryCard(
    state: MainDisplayState,
    modifier: Modifier = Modifier,
) {
    val totalPerPersonDescription = stringResource(
        R.string.total_per_person_content_description,
        state.totalPerPersonText,
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.results_section_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.semantics { heading() },
            )

            Text(
                text = stringResource(R.string.total_per_person_title),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 20.dp),
            )
            Text(
                text = state.totalPerPersonText,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics {
                    contentDescription = totalPerPersonDescription
                },
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            SummaryRow(label = stringResource(R.string.total_to_pay_title), value = state.totalToPayText)
            SummaryRow(label = stringResource(R.string.total_tip_title), value = state.totalTipText)
            SummaryRow(label = stringResource(R.string.tip_per_person_title), value = state.tipPerPersonText)
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    val rowDescription = stringResource(R.string.summary_row_content_description, label, value)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .semantics {
                contentDescription = rowDescription
            },
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
private fun TipSummaryCardPreview() {
    TipCalculatorComposeTheme {
        TipSummaryCard(
            state = MainDisplayState(
                billInput = "12.5",
                tipPercent = 15,
                isCustomTip = false,
                splitNum = 2,
                roundUp = true,
                totalToPayText = "$14.00",
                totalPerPersonText = "$7.00",
                totalTipText = "$1.50",
                tipPerPersonText = "$0.75",
                tipPercentText = "15 %",
                isSplitDecreaseEnabled = true,
                isSplitIncreaseEnabled = true,
            ),
        )
    }
}
