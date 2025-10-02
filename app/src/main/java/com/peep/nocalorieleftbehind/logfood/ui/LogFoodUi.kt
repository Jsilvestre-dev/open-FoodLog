package com.peep.nocalorieleftbehind.logfood.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.ui.model.NameUiState
import com.peep.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.peep.nocalorieleftbehind.core.util.State

@Immutable
data class LogFoodUi(
    val state: State = State.Loading,
    val nameUiState: NameUiState = NameUiState(),
    val nutritionUi: NutritionUiState = NutritionUiState(),
    val errorMessage: Int? = null
) {

    fun isLogFoodUiValid(): Boolean =
        nameUiState.state is State.Success && nutritionUi.areAllNutrientsValid()
}