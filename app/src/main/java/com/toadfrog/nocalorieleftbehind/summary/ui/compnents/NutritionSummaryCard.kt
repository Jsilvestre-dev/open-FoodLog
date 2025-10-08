package com.toadfrog.nocalorieleftbehind.summary.ui.compnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.toadfrog.nocalorieleftbehind.summary.ui.model.NutrientSummaryUiState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NutritionSummaryCard(
    modifier: Modifier = Modifier,
    nutrition: () -> List<NutrientSummaryUiState>,
) {
    Column(
        modifier = Modifier.padding(12.dp)
    ) {

        nutrition().forEach {
            NutrientItem(
                nutrientSummaryUiState = it
            )
        }
    }
}

@Composable
private fun NutrientItem(nutrientSummaryUiState: NutrientSummaryUiState) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(nutrientSummaryUiState.nutrient.nameResId),
            fontFamily = notoSansFamily,
            style = MaterialTheme.typography.titleMedium,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            LinearProgressIndicator(
                modifier = Modifier.height(8.dp),
                progress = {
                    nutrientSummaryUiState.eaten.toFloat() / nutrientSummaryUiState.total.toFloat()
                }
            )

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = nutrientSummaryUiState.eaten.toString().plus(nutrientSummaryUiState.nutrient.unit),
                    fontFamily = notoSansFamily
                )
                Text(
                    text = "/",
                    fontFamily = notoSansFamily
                )

                Text(
                    text = nutrientSummaryUiState.total.toString().plus(nutrientSummaryUiState.nutrient.unit),
                    fontFamily = notoSansFamily

                )
            }
        }
    }

}

@Composable
@Preview
private fun Preview() {
    NoCalorieLeftBehindTheme {
        NutritionSummaryCard(
            modifier = Modifier.fillMaxWidth(),
            nutrition = {
                listOf<NutrientSummaryUiState>(
                    NutrientSummaryUiState(
                        nutrient = Nutrient.PROTEIN,
                        eaten = 0,
                        left = 0,
                        total = 100
                    ),

                    NutrientSummaryUiState(
                        nutrient = Nutrient.CARBS,
                        eaten = 0,
                        left = 0,
                        total = 100
                    ),
                    NutrientSummaryUiState(
                        nutrient = Nutrient.FATS,
                        eaten = 50,
                        left = 0,
                        total = 100
                    )

                )
            }
        )
    }
}