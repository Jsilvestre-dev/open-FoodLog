package com.toadfrog.nocalorieleftbehind.logfood.data

import com.toadfrog.nocalorieleftbehind.core.domain.model.Food
import com.toadfrog.nocalorieleftbehind.core.ui.model.NameUiState
import com.toadfrog.nocalorieleftbehind.core.ui.toNutritionUi
import com.toadfrog.nocalorieleftbehind.logfood.ui.LogFoodUi

fun Food.toLogFoodUi(): LogFoodUi {
    return LogFoodUi(
        nameUiState = NameUiState(name = name),
        nutritionUi = nutrition.toNutritionUi()
    )
}