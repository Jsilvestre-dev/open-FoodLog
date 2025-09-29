package com.peep.nocalorieleftbehind.preference.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.domain.ValidateNutrientUseCase
import com.peep.nocalorieleftbehind.core.domain.model.Preferences
import com.peep.nocalorieleftbehind.core.ui.NutritionUi
import com.peep.nocalorieleftbehind.core.ui.toNutrition
import com.peep.nocalorieleftbehind.core.ui.toNutritionUi
import com.peep.nocalorieleftbehind.core.util.UiElement
import com.peep.nocalorieleftbehind.preference.data.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferenceViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val validateNutrientUseCase: ValidateNutrientUseCase
) : ViewModel() {

    private val _screenUiFlow: MutableStateFlow<UiElement<*>> = MutableStateFlow(UiElement.Success(null))
    val screenUiFlow = _screenUiFlow.asStateFlow()

    private val _updatedNutrientUiFlow = MutableStateFlow<NutrientUi?>(null)
    val updatedNutrientUiFlow = _updatedNutrientUiFlow.asStateFlow()

    private val _nutritionUiFlow = preferenceRepository
        .getPreference()
        .map { preferences ->
            preferences?.nutrition?.toNutritionUi() ?: NutritionUi()
        }
    val nutritionUi: StateFlow<NutritionUi> = _nutritionUiFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NutritionUi()
    )

    fun onUpdateNutrientUi(nutrient: Nutrient?) {
        viewModelScope.launch {

            if (nutrient == null) return@launch _updatedNutrientUiFlow.update { null }

            _updatedNutrientUiFlow.update { nutritionUi ->
                NutrientUi(
                    nutrient = nutrient,
                    ui = this@PreferenceViewModel.nutritionUi.value.getNutrientUi(nutrient) ?: UiElement.Loading
                )
            }
        }
    }

    fun onSelected(selectedNutrients: List<Nutrient>) {
        viewModelScope.launch {
            if (selectedNutrients.isEmpty()) return@launch

            var currentNutritionUi = nutritionUi.value
            selectedNutrients.forEach { nutrient ->
                currentNutritionUi = currentNutritionUi.updateNutrientUi(nutrient = nutrient, UiElement.Success("0"))
            }

            preferenceRepository.savePreference(
                Preferences(
                    nutrition = currentNutritionUi.toNutrition()
                )
            )
        }
    }

    fun onInput(nutrientData: NutrientData) {
        viewModelScope.launch {
            val validatedNutrientValue = validateNutrientUseCase(nutrientData.value)

            _updatedNutrientUiFlow.update { nutritionUi ->
                NutrientUi(
                    nutrient = nutrientData.nutrient,
                    ui = validatedNutrientValue
                )
            }
        }
    }

    fun onRemove(nutrient: Nutrient) {
        viewModelScope.launch {
            val currentNutritionUi = nutritionUi.value.updateNutrientUi(nutrient = nutrient, uiState = null)

            preferenceRepository.savePreference(
                Preferences(
                    nutrition = currentNutritionUi.toNutrition()
                )
            )
        }
    }

    fun savePreference() {
        viewModelScope.launch {
            _screenUiFlow.update { UiElement.Loading }

            val currentUpdatedNutrientUi = _updatedNutrientUiFlow.value

            if (currentUpdatedNutrientUi == null || currentUpdatedNutrientUi.ui !is UiElement.Success) return@launch _screenUiFlow.update {
                UiElement.Success(
                    null
                )
            }

            val nutrition = nutritionUi.value.toNutrition().updateNutrient(
                nutrient = currentUpdatedNutrientUi.nutrient,
                value = currentUpdatedNutrientUi.ui.data.toInt()
            )

            preferenceRepository.savePreference(
                Preferences(
                    nutrition = nutrition
                )
            )

            _screenUiFlow.update { UiElement.Success(null) }
        }
    }
}