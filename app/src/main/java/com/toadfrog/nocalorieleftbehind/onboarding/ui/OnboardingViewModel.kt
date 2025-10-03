package com.toadfrog.nocalorieleftbehind.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toadfrog.nocalorieleftbehind.core.data.repository.PreferenceRepository
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.domain.ValidateNutrientAmountUseCase
import com.toadfrog.nocalorieleftbehind.core.domain.model.Preference
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientUiState
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.toadfrog.nocalorieleftbehind.core.ui.toNutrition
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientInput
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class OnboardingViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val validateNutrientAmountUseCase: ValidateNutrientAmountUseCase
) : ViewModel() {
    private val _onboardingUiState = MutableStateFlow<OnboardingUiState>(
        value = OnboardingUiState(
            state = State.Success
        )
    )
    val onboardingUiState: StateFlow<OnboardingUiState> = _onboardingUiState.asStateFlow()

    fun onTrackNutrient(nutrient: Nutrient) {
        viewModelScope.launch {
            _onboardingUiState.update { onboardingUiState ->
                val nutritionUiState = if (onboardingUiState.nutritionUiState.getNutrientUi(nutrient) != null) {
                    onboardingUiState.nutritionUiState.removeNutrient(nutrient)
                } else {
                    onboardingUiState.nutritionUiState.updateNutrient(
                        nutrient = nutrient,
                        nutrientUiState = NutrientUiState(nutrient = nutrient)
                    )
                }
                onboardingUiState.copy(nutritionUiState = nutritionUiState)
            }
        }
    }

    fun onNutrientGoalInput(nutrientInput: NutrientInput) {
        viewModelScope.launch {
            _onboardingUiState.update { onboardingUiState ->
                onboardingUiState.copy(
                    nutritionUiState = onboardingUiState.nutritionUiState.updateNutrient(
                        nutrient = nutrientInput.nutrient,
                        nutrientUiState = validateNutrientAmountUseCase(nutrientInput = nutrientInput)
                    )
                )
            }
        }
    }

    fun savePreference(onCompletion: () -> Unit = {}) {
        viewModelScope.launch {
            val onboardingUiState = _onboardingUiState.value

            _onboardingUiState.update { it.copy(state = State.Loading) }

            val validatedNutritionUiState = NutritionUiState(
                calories = validateNutrientAmountUseCase(nutrientUiState = onboardingUiState.nutritionUiState.calories),
                protein = onboardingUiState.nutritionUiState.protein?.let {
                    validateNutrientAmountUseCase(
                        nutrientUiState = it
                    )
                },
                carbs = onboardingUiState.nutritionUiState.carbs?.let { validateNutrientAmountUseCase(nutrientUiState = it) },
                fats = onboardingUiState.nutritionUiState.fats?.let { validateNutrientAmountUseCase(nutrientUiState = it) }
            )

            if (!validatedNutritionUiState.areAllNutrientsValid()) {
                _onboardingUiState.update {
                    it.copy(
                        state = State.Success,
                        nutritionUiState = validatedNutritionUiState
                    )
                }
                return@launch
            }

            val preference = Preference(
                nutrition = validatedNutritionUiState.toNutrition()
            )

            preferenceRepository.updatePreference(preference)

            onCompletion()
        }
    }
}