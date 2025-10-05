package com.toadfrog.nocalorieleftbehind.summary.ui.compnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.toadfrog.nocalorieleftbehind.summary.ui.model.NutrientSummaryUiState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NutrientSummaryElement(
    modifier: Modifier = Modifier,
    nutrientSummaryUiState: NutrientSummaryUiState,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(116.dp),
            shape = MaterialShapes.Sunny.toShape(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize().fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = nutrientSummaryUiState.eaten.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = notoSansFamily,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = nutrientSummaryUiState.left.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = notoSansFamily,
                        fontWeight = FontWeight.Bold
                    )

                }

                Spacer(modifier = Modifier.width(4.dp))

                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .height(IntrinsicSize.Max),
                ) {
                    Text(
                        text = "eaten",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = notoSansFamily,
                    )

                    Text(
                        text = "left",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = notoSansFamily,
                    )
                }
            }
        }

        Text(
            text = stringResource(nutrientSummaryUiState.nutrient.nameResId),
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = notoSansFamily,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NoCalorieLeftBehindTheme {
        NutrientSummaryElement(
            modifier = Modifier,
            nutrientSummaryUiState = NutrientSummaryUiState(
                nutrient = Nutrient.PROTEIN,
                eaten = 82,
                left = 68,
                total = 150
            )
        )
    }

}