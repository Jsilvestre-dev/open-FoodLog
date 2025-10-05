package com.toadfrog.nocalorieleftbehind.logfood.ui

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import com.toadfrog.nocalorieleftbehind.core.ui.model.NameUiState
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.toadfrog.nocalorieleftbehind.core.util.State

@Immutable
@Keep
data class LogFoodUi(
    val id: Long? = null,
    val state: State = State.Loading,
    val nameUiState: NameUiState = NameUiState(),
    val nutritionUi: NutritionUiState = NutritionUiState(),
    val errorMessage: Int? = null
) {

    fun isLogFoodUiValid(): Boolean =
        nameUiState.state is State.Success && nutritionUi.areAllNutrientsValid()
}