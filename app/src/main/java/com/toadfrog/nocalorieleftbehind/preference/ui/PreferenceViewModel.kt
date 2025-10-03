package com.toadfrog.nocalorieleftbehind.preference.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.domain.ValidateNutrientAmountUseCase
import com.toadfrog.nocalorieleftbehind.core.domain.model.Preference
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientUiState
import com.toadfrog.nocalorieleftbehind.core.ui.toNutrition
import com.toadfrog.nocalorieleftbehind.core.ui.toNutritionUi
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.core.data.repository.PreferenceRepository
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferenceViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val validateNutrientAmountUseCase: ValidateNutrientAmountUseCase
) : ViewModel() {
    private val _selectedNutrientUiState = MutableStateFlow<NutrientUiState?>(null)
    val selectedNutrientUiState = _selectedNutrientUiState.asStateFlow()

    private val _preferenceUiState = preferenceRepository
        .getPreference()
        .map { preferences ->
            preferences ?: throw NullPointerException()

            PreferenceUiState(
                state = State.Success,
                nutritionUiState = preferences.nutrition.toNutritionUi()
            )
        }.catch {
            PreferenceUiState(
                state = State.Error,
                errorMessage = R.string.try_refreshing
            )
        }

    val preferenceUiState: StateFlow<PreferenceUiState> = _preferenceUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PreferenceUiState()
    )

    fun onEditNutrient(nutrient: Nutrient?) {
        viewModelScope.launch {

            if (nutrient == null) return@launch _selectedNutrientUiState.update { null }

            _selectedNutrientUiState.update {
                preferenceUiState.value.nutritionUiState.getNutrientUi(nutrient)
            }
            println("onEditNutrient ${_selectedNutrientUiState.value}")
        }
    }

    fun onTrackNutrients(selectedNutrients: List<Nutrient>) {
        viewModelScope.launch {
            if (selectedNutrients.isEmpty()) return@launch

            var nutritionUiState = preferenceUiState.value.nutritionUiState
            selectedNutrients.forEach { nutrient ->
                nutritionUiState =
                    nutritionUiState.updateNutrient(
                        nutrient = nutrient,
                        nutrientUiState = NutrientUiState(nutrient = nutrient, data = "0")
                    )
            }

            preferenceRepository.updatePreference(
                Preference(
                    nutrition = nutritionUiState.toNutrition()
                )
            )
        }
    }

    fun onInput(nutrientInput: NutrientInput) {
        viewModelScope.launch {
            val validatedNutrientValue =
                validateNutrientAmountUseCase(nutrientInput)

            _selectedNutrientUiState.update { nutritionUi ->
                validatedNutrientValue
            }
        }
    }

    fun onRemove(nutrient: Nutrient) {
        viewModelScope.launch {
            val currentNutritionUi = preferenceUiState.value.nutritionUiState.removeNutrient(nutrient)

            preferenceRepository.updatePreference(
                Preference(
                    nutrition = currentNutritionUi.toNutrition()
                )
            )
        }
    }

    fun savePreference() {
        viewModelScope.launch {

            val nutrientUiState = _selectedNutrientUiState.value

            _preferenceUiState.map { it.copy(state = State.Loading) }

            if (nutrientUiState == null || nutrientUiState.state !is State.Success) {
                _preferenceUiState.map { it.copy(state = State.Success) }
                return@launch
            }

            val nutrition = preferenceUiState.value.nutritionUiState.updateNutrient(
                nutrient = nutrientUiState.nutrient,
                nutrientUiState = nutrientUiState
            ).toNutrition()

            preferenceRepository.updatePreference(
                Preference(
                    nutrition = nutrition
                )
            )

            _preferenceUiState.map { it.copy(state = State.Success) }
        }
    }
}