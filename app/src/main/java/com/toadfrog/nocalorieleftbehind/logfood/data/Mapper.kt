package com.toadfrog.nocalorieleftbehind.logfood.data

import com.toadfrog.nocalorieleftbehind.core.domain.model.Food
import com.toadfrog.nocalorieleftbehind.core.ui.model.NameUiState
import com.toadfrog.nocalorieleftbehind.core.ui.toNutritionUi
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.logfood.ui.LogFoodUi
import com.toadfrog.nocalorieleftbehind.summary.ui.model.FoodUiState

fun Food.toLogFoodUi(): LogFoodUi {
    return LogFoodUi(
        id = id,
        state = State.Success,
        nameUiState = NameUiState(
            state = State.Success,
            name = name
        ),
        nutritionUi = nutrition.toNutritionUi(state = State.Success)
    )
}