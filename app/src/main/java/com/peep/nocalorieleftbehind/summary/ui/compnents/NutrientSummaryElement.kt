package com.peep.nocalorieleftbehind.summary.ui.compnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.peep.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.peep.nocalorieleftbehind.summary.ui.NutrientSummaryUiState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NutrientSummaryElement(
    modifier: Modifier = Modifier,
    nutrientSummaryUiState: NutrientSummaryUiState,
) {
    Card(
        modifier = modifier,
        shape = MaterialShapes.Sunny.toShape(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nutrientSummaryUiState.eaten.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = notoSansFamily,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "eaten",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = notoSansFamily,
                )
            }

            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(nutrientSummaryUiState.nutrient.iconResId),
                    contentDescription = null,

                    )
                Text(
                    text = stringResource(nutrientSummaryUiState.nutrient.nameResId),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = notoSansFamily,
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NoCalorieLeftBehindTheme {
        NutrientSummaryElement(
            modifier = Modifier.aspectRatio(1f),
            nutrientSummaryUiState = NutrientSummaryUiState(
                nutrient = Nutrient.PROTEIN,
                eaten = 82,
                left = 68,
                total = 150
            )
        )
    }

}