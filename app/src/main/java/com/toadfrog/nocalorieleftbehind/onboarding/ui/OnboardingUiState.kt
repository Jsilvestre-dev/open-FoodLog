package com.toadfrog.nocalorieleftbehind.onboarding.ui

import com.toadfrog.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.toadfrog.nocalorieleftbehind.core.util.State

data class OnboardingUiState(
    val state: State = State.Loading,
    val nutritionUiState: NutritionUiState = NutritionUiState(),
    val errorMessage: Int? = null
)