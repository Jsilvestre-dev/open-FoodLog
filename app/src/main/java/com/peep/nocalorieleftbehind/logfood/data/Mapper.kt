package com.peep.nocalorieleftbehind.logfood.data

import com.peep.nocalorieleftbehind.core.domain.model.Food
import com.peep.nocalorieleftbehind.core.ui.toNutritionUi
import com.peep.nocalorieleftbehind.core.util.UiState
import com.peep.nocalorieleftbehind.logfood.ui.LogFoodUi

fun Food.toLogFoodUi(): LogFoodUi {
    return LogFoodUi(
        foodNameUi = UiState.Success(name),
        nutritionUi = nutrition.toNutritionUi()
    )
}