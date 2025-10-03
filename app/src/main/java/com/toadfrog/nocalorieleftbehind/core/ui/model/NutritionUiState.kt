package com.toadfrog.nocalorieleftbehind.core.ui.model

import androidx.compose.runtime.Immutable
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.util.State

@Immutable
data class NutritionUiState(
    val calories: NutrientUiState = NutrientUiState(
        state = State.Loading,
        nutrient = Nutrient.CALORIES,
        data = "0",
        errorMessage = null
    ),
    val protein: NutrientUiState? = null,
    val carbs: NutrientUiState? = null,
    val fats: NutrientUiState? = null
) {

    fun areAllNutrientsValid(): Boolean {
        val isProteinValid = protein?.let { it.state is State.Success } ?: true
        val isCarbsValid = carbs?.let { it.state is State.Success } ?: true
        val isFatsValid = fats?.let { it.state is State.Success } ?: true
        return calories.state is State.Success && isProteinValid && isCarbsValid && isFatsValid
    }

    fun updateNutrient(nutrient: Nutrient, nutrientUiState: NutrientUiState): NutritionUiState {
        return when (nutrient) {
            Nutrient.CALORIES -> copy(calories = nutrientUiState)
            Nutrient.PROTEIN -> copy(protein = nutrientUiState)
            Nutrient.FATS -> copy(fats = nutrientUiState)
            Nutrient.CARBS -> copy(carbs = nutrientUiState)
        }
    }

    fun removeNutrient(nutrient: Nutrient): NutritionUiState = when (nutrient) {
        Nutrient.CALORIES -> this
        Nutrient.PROTEIN -> copy(protein = null)
        Nutrient.FATS -> copy(fats = null)
        Nutrient.CARBS -> copy(carbs = null)
    }


    fun getNutrientUi(nutrient: Nutrient): NutrientUiState? = when (nutrient) {
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