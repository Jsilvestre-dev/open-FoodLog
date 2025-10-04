package com.toadfrog.nocalorieleftbehind.onboarding.ui

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.toadfrog.nocalorieleftbehind.core.util.State

@Immutable
@Keep
data class OnboardingUiState(
    val state: State = State.Loading,
    val nutritionUiState: NutritionUiState = NutritionUiState(),
    val errorMessage: Int? = null
)