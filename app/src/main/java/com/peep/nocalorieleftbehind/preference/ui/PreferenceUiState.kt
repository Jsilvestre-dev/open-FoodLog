package com.peep.nocalorieleftbehind.preference.ui

import com.peep.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.peep.nocalorieleftbehind.core.util.State

data class PreferenceUiState(
    val state: State = State.Loading,
    val nutritionUiState: NutritionUiState = NutritionUiState(),
    val errorMessage: Int? = null
)