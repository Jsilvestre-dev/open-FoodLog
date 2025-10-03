package com.toadfrog.nocalorieleftbehind.core.ui

import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.domain.model.Nutrition
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientUiState
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.toadfrog.nocalorieleftbehind.core.util.State

fun Nutrition.toNutritionUi(copyData: Boolean = true) = NutritionUiState(
    calories = NutrientUiState(
        state = State.Loading,
        nutrient = Nutrient.CALORIES,
        data = if (copyData) calories.toString() else ""
    ),
    protein = protein?.let { int ->
        NutrientUiState(
            state = State.Loading,
            nutrient = Nutrient.PROTEIN,
            data = if (copyData) int.toString() else ""
        )
    },
    carbs = carbs?.let { int ->
        NutrientUiState(
            state = State.Loading,
            nutrient = Nutrient.CARBS,
            data = if (copyData) int.toString() else ""
        )
    },
    fats = fats?.let { int ->
        NutrientUiState(
            state = State.Loading,
            nutrient = Nutrient.FATS,
            data = if (copyData) int.toString() else ""
        )
    }
)

fun NutritionUiState.toNutrition() = Nutrition(
    calories = calories.data.toInt(),
    protein = protein?.data?.toInt(),
    carbs = carbs?.data?.toInt(),
    fats = fats?.data?.toInt()
)