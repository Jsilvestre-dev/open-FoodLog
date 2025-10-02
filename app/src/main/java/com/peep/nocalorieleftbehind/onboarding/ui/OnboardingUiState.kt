package com.peep.nocalorieleftbehind.onboarding.ui

import com.peep.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.peep.nocalorieleftbehind.core.util.State

data class OnboardingUiState(
    val state: State = State.Loading,
    val nutritionUiState: NutritionUiState = NutritionUiState(),
    val errorMessage: Int? = null
)