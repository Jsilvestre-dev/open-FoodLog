package com.peep.nocalorieleftbehind.logfood.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.ui.NutritionUi
import com.peep.nocalorieleftbehind.core.util.UiState

@Immutable
data class LogFoodUi(
    val foodNameUi: UiState<String> = UiState.Loading,
    val nutritionUi: NutritionUi = NutritionUi(),
) {

    fun isLogFoodUiValid(): Boolean =
        foodNameUi is UiState.Success && nutritionUi.areAllNutrientsValid()
}