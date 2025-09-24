package com.peep.nocalorieleftbehind.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peep.nocalorieleftbehind.core.domain.ValidateNutrientUseCase
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.domain.model.Nutrition
import com.peep.nocalorieleftbehind.core.domain.model.Preferences
import com.peep.nocalorieleftbehind.core.ui.NutritionUi
import com.peep.nocalorieleftbehind.core.util.UiState
import com.peep.nocalorieleftbehind.preference.data.PreferenceRepository
import com.peep.nocalorieleftbehind.preference.ui.NutrientData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class OnboardingViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val validateNutrientUseCase: ValidateNutrientUseCase
) : ViewModel() {

    private val _screenUiFlow = MutableStateFlow<UiState<*>>(UiState.Success(null))
    val screenUiFlow = _screenUiFlow.asStateFlow()

    private val _nutritionUiFlow = MutableStateFlow<NutritionUi>(NutritionUi())
    val nutritionUiFlow: StateFlow<NutritionUi> = _nutritionUiFlow.asStateFlow()

    fun onSelected(nutrient: Nutrient) {
        viewModelScope.launch {
            if (_screenUiFlow.value !is UiState.Success) return@launch
            _nutritionUiFlow.update { nutritionUi ->
                val uiState = if (nutritionUi.trackedNutrients().contains(nutrient)) {
                    null
                } else {
                    UiState.Success("0")
                }
                nutritionUi.updateNutrientUi(nutrient = nutrient, uiState = uiState)
            }
        }
    }

    fun onInput(nutrientData: NutrientData) {
        viewModelScope.launch {
            _nutritionUiFlow.update { nutritionUi ->
                nutritionUi.updateNutrientUi(
                    nutrient = nutrientData.nutrient,
                    uiState = validateNutrientUseCase(nutrientData.value)
                )
            }
        }
    }

    fun savePreference(onCompletion: () -> Unit = {}) {
        viewModelScope.launch {
            if (_screenUiFlow.value !is UiState.Success) return@launch

            _screenUiFlow.update { UiState.Loading }

            val currentNutritionUi = _nutritionUiFlow.value

            val validatedNutritionUi = NutritionUi(
                calories = validateNutrientUseCase(currentNutritionUi.calories),
                protein = currentNutritionUi.protein?.let { validateNutrientUseCase(it) },
                carbs = currentNutritionUi.carbs?.let { validateNutrientUseCase(it) },
                fats = currentNutritionUi.fats?.let { validateNutrientUseCase(it) }
            )

            if (!validatedNutritionUi.areAllNutrientsValid()) {
                _nutritionUiFlow.update { validatedNutritionUi }
                _screenUiFlow.update { UiState.Success(null) }
                return@launch
            }

            val preferences = validatedNutritionUi.let {
                Preferences(
                    nutrition = Nutrition(
                        calories = (it.calories as UiState.Success<String>).data.toInt(),
                        protein = (it.protein as? UiState.Success<String>)?.data?.toInt(),
                        carbs = (it.carbs as? UiState.Success<String>)?.data?.toInt(),
                        fats = (it.fats as? UiState.Success<String>)?.data?.toInt()
                    ),
                )
            }

            preferenceRepository.savePreference(preferences)

            onCompletion()
        }
    }
}