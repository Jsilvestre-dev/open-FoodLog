package com.toadfrog.nocalorieleftbehind.onboarding.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.ui.theme.montserratFamily

@Composable
fun NutrientSelectionUi(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    selectableNutrients: List<Nutrient>,
    trackedNutrients: () -> List<Nutrient>,
    onTrackNutrient: (Nutrient) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(
            key = "title",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = montserratFamily,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                text = "Nutrient Selection"
            )
        }

        items(
            items = selectableNutrients,
            key = { it.name },
            contentType = { it }
        ) { nutrient ->
            NutrientCard(
                isSelected = trackedNutrients().contains(nutrient),
                nutrient = nutrient,
                onClick = {
                    onTrackNutrient(nutrient)
                }
            )
        }
    }
}