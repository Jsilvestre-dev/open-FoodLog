package com.peep.nocalorieleftbehind.summary.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.domain.Nutrient

@Immutable
data class SummaryUi(
    val calories: NutrientSummary,
    val protein: NutrientSummary?,
    val carbs: NutrientSummary?,
    val fats: NutrientSummary?,
    val date: String
){
    companion object{
        val default = SummaryUi(
            calories = NutrientSummary(
                nutrient = Nutrient.CALORIES,
                eaten = 0,
                left = 0,
                total = 0
            ),
            protein = NutrientSummary(
                nutrient = Nutrient.PROTEIN,
                eaten = 0,
                left = 0,
                total = 0
            ),
            carbs = NutrientSummary(
                nutrient = Nutrient.CARBS,
                eaten = 0,
                left = 0,
                total = 0
            ),
            fats = NutrientSummary(
                nutrient = Nutrient.FATS,
                eaten = 0,
                left = 0,
                total = 0
            ),
            date = "Apr 1, 2025"
        )
    }
}
