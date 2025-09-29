package com.peep.nocalorieleftbehind.core.ui

import com.peep.nocalorieleftbehind.core.domain.model.Nutrition
import com.peep.nocalorieleftbehind.core.util.UiElement

fun Nutrition.toNutritionUiLoading() = NutritionUi(
    calories = UiElement.Loading,
    protein = protein?.let { UiElement.Loading },
    carbs = carbs?.let { UiElement.Loading },
    fats = fats?.let { UiElement.Loading }
)

fun Nutrition.toNutritionUi() = NutritionUi(
    calories = UiElement.Success(calories.toString()),
    protein = protein?.let { UiElement.Success(it.toString()) },
    carbs = carbs?.let { UiElement.Success(it.toString()) },
    fats = fats?.let { UiElement.Success(it.toString()) }
)

fun NutritionUi.toNutrition() = Nutrition(
    calories = (calories as UiElement.Success).data.toInt(),
    protein = (protein as? UiElement.Success)?.data?.toInt(),
    carbs = (carbs as? UiElement.Success)?.data?.toInt(),
    fats = (fats as? UiElement.Success)?.data?.toInt()
)