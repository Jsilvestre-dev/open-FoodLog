package com.peep.nocalorieleftbehind.core.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.util.UiElement

@Immutable
data class NutritionUi(
    val calories: UiElement<String> = UiElement.Loading,
    val protein: UiElement<String>? = null,
    val carbs: UiElement<String>? = null,
    val fats: UiElement<String>? = null
) {

    fun areAllNutrientsValid(): Boolean {
        val isProteinValid = protein?.let { it is UiElement.Success } ?: true
        val isCarbsValid = carbs?.let { it is UiElement.Success } ?: true
        val isFatsValid = fats?.let { it is UiElement.Success } ?: true
        return calories is UiElement.Success && isProteinValid && isCarbsValid && isFatsValid
    }

    fun updateNutrientUi(nutrient: Nutrient, uiState: UiElement<String>?) = when (nutrient) {
        Nutrient.CALORIES -> if (uiState != null) copy(calories = uiState) else this
        Nutrient.PROTEIN -> copy(protein = uiState)
        Nutrient.FATS -> copy(fats = uiState)
        Nutrient.CARBS -> copy(carbs = uiState)
    }

    fun getNutrientUi(nutrient: Nutrient): UiElement<String>? = when (nutrient) {
        Nutrient.CALORIES -> calories
        Nutrient.PROTEIN -> protein
        Nutrient.FATS -> fats
        Nutrient.CARBS -> carbs
    }

    fun trackedNutrients(includeCalories: Boolean = false): List<Nutrient> = buildList {
        if (includeCalories) add(Nutrient.CALORIES)
        protein?.let { add(Nutrient.PROTEIN) }
        carbs?.let { add(Nutrient.CARBS) }
        fats?.let { add(Nutrient.FATS) }
    }

    fun untrackedNutrients(): List<Nutrient> = buildList {
        protein ?: add(Nutrient.PROTEIN)
        carbs ?: add(Nutrient.CARBS)
        fats ?: add(Nutrient.FATS)
    }

}