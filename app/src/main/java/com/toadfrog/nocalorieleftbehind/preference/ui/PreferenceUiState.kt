package com.toadfrog.nocalorieleftbehind.preference.ui

import com.toadfrog.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.toadfrog.nocalorieleftbehind.core.util.State

data class PreferenceUiState(
    val state: State = State.Loading,
    val nutritionUiState: NutritionUiState = NutritionUiState(),
    val errorMessage: Int? = null
)