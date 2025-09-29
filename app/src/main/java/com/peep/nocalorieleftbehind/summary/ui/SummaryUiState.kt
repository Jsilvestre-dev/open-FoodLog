package com.peep.nocalorieleftbehind.summary.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.domain.Nutrient

@Immutable
data class SummaryUiState(
    val calories: NutrientSummaryUiState,
    val nutrientSummaryList: List<NutrientSummaryUiState>,
    val date: String
) {
    companion object {
        val default = SummaryUiState(
            calories = NutrientSummaryUiState(
                nutrient = Nutrient.CALORIES,
                eaten = 0,
                left = 0,
                total = 0
            ),
            nutrientSummaryList = buildList {
                add(
                    NutrientSummaryUiState(
                        nutrient = Nutrient.PROTEIN,
                        eaten = 0,
                        left = 0,
                        total = 0
                    )
                )
                add(
                    NutrientSummaryUiState(
                        nutrient = Nutrient.CARBS,
                        eaten = 0,
                        left = 0,
                        total = 0
                    )
                )
                add(
                    NutrientSummaryUiState(
                        nutrient = Nutrient.FATS,
                        eaten = 0,
                        left = 0,
                        total = 0
                    )
                )
            },
            date = "Apr 1"
        )
    }
}
