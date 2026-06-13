package com.dendron.tipcalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dendron.tipcalculator.ui.theme.TipCalculatorComposeTheme

@Composable
fun TipSummaryCard(
    state: MainDisplayState,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Summary",
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(
                text = "Total per person",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 20.dp),
            )
            Text(
                text = state.totalPerPersonText,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            SummaryRow(label = "Total to pay", value = state.totalToPayText)
            SummaryRow(label = "Total tip", value = state.totalTipText)
            SummaryRow(label = "Tip per person", value = state.tipPerPersonText)
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
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
