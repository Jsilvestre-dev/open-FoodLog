package com.toadfrog.nocalorieleftbehind.summary.ui.model

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.util.State

@Immutable
@Keep
data class SummaryUiState(
    val state: State = State.Loading,
    val calories: NutrientSummaryUiState,
    val nutrientSummaryList: List<NutrientSummaryUiState>,
    val date: String,
    val errorMessage: Int? = null
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
