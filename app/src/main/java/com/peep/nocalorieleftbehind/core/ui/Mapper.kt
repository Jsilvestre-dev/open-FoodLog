package com.peep.nocalorieleftbehind.core.ui

import com.peep.nocalorieleftbehind.core.domain.model.Nutrition
import com.peep.nocalorieleftbehind.core.util.UiState

fun Nutrition.toNutritionUiLoading() = NutritionUi(
    calories = UiState.Loading,
    protein = protein?.let { UiState.Loading },
    carbs = carbs?.let { UiState.Loading },
    fats = fats?.let { UiState.Loading }
)

fun Nutrition.toNutritionUi() = NutritionUi(
    calories = UiState.Success(calories.toString()),
    protein = protein?.let { UiState.Success(it.toString()) },
    carbs = carbs?.let { UiState.Success(it.toString()) },
    fats = fats?.let { UiState.Success(it.toString()) }
)

fun NutritionUi.toNutrition() = Nutrition(
    calories = (calories as UiState.Success).data.toInt(),
    protein = (protein as? UiState.Success)?.data?.toInt(),
    carbs = (carbs as? UiState.Success)?.data?.toInt(),
    fats = (fats as? UiState.Success)?.data?.toInt()
)