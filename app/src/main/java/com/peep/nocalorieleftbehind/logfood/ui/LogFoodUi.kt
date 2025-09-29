package com.peep.nocalorieleftbehind.logfood.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.ui.NutritionUi
import com.peep.nocalorieleftbehind.core.util.Ui
import com.peep.nocalorieleftbehind.core.util.UiElement

@Immutable
data class LogFoodUi(
    val uiState: Ui = Ui.Loading,
    val foodNameUi: UiElement<String> = UiElement.Loading,
    val nutritionUi: NutritionUi = NutritionUi(),
) {

    fun isLogFoodUiValid(): Boolean =
        foodNameUi is UiElement.Success && nutritionUi.areAllNutrientsValid()
}