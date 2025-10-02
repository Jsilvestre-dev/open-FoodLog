package com.peep.nocalorieleftbehind.logfood.data

import com.peep.nocalorieleftbehind.core.domain.model.Food
import com.peep.nocalorieleftbehind.core.ui.model.NameUiState
import com.peep.nocalorieleftbehind.core.ui.toNutritionUi
import com.peep.nocalorieleftbehind.logfood.ui.LogFoodUi

fun Food.toLogFoodUi(): LogFoodUi {
    return LogFoodUi(
        nameUiState = NameUiState(name = name),
        nutritionUi = nutrition.toNutritionUi()
    )
}