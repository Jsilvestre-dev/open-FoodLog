package com.peep.nocalorieleftbehind.summary.ui.compnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peep.nocalorieleftbehind.R
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.peep.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.peep.nocalorieleftbehind.summary.ui.NutrientSummaryUiState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Header(
    caloriesSummary: () -> NutrientSummaryUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(.6f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.calories),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontFamily = montserratFamily,
            )

            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = caloriesSummary().eaten.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayMedium,
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = caloriesSummary().left.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayMedium,
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {

                    Text(
                        text = "eaten",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = montserratFamily,
                    )

                    Text(
                        text = "left",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = montserratFamily,
                    )
                }

            }
        }

        CircularWavyProgressIndicator(
            modifier = Modifier
                .weight(.4f)
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .aspectRatio(1f),
            progress = { caloriesSummary().let { it.eaten.toFloat() / it.total.toFloat() } },
            stroke = Stroke(
                width =
                    with(LocalDensity.current) {
                        6.dp.toPx()
                    },
                cap = StrokeCap.Round,
            ),
            trackStroke = Stroke(
                width =
                    with(LocalDensity.current) {
                        6.dp.toPx()
                    },
                cap = StrokeCap.Round,
            ),
            wavelength = 32.dp,
            waveSpeed = 4.dp
        )
    }
}

@Preview
@Composable
private fun Preview() {
    NoCalorieLeftBehindTheme {
        Header(
            caloriesSummary = {
                NutrientSummaryUiState(
                    nutrient = Nutrient.CALORIES,
                    eaten = 500,
                    left = 2500,
                    total = 1500
                )
            }
        )
    }
}