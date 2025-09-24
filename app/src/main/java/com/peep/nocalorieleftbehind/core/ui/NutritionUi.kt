package com.peep.nocalorieleftbehind.core.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.util.UiState

@Immutable
data class NutritionUi(
    val calories: UiState<String> = UiState.Loading,
    val protein: UiState<String>? = null,
    val carbs: UiState<String>? = null,
    val fats: UiState<String>? = null
) {

    fun areAllNutrientsValid(): Boolean {
        val isProteinValid = protein?.let { it is UiState.Success } ?: true
        val isCarbsValid = carbs?.let { it is UiState.Success } ?: true
        val isFatsValid = fats?.let { it is UiState.Success } ?: true
        return calories is UiState.Success && isProteinValid && isCarbsValid && isFatsValid
    }

    fun updateNutrientUi(nutrient: Nutrient, uiState: UiState<String>?) = when (nutrient) {
        Nutrient.CALORIES -> if (uiState != null) copy(calories = uiState) else this
        Nutrient.PROTEIN -> copy(protein = uiState)
        Nutrient.FATS -> copy(fats = uiState)
        Nutrient.CARBS -> copy(carbs = uiState)
    }

    fun getNutrientUi(nutrient: Nutrient): UiState<String>? = when (nutrient) {
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