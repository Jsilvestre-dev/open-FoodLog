package com.toadfrog.nocalorieleftbehind.logfood.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.data.repository.FoodRepository
import com.toadfrog.nocalorieleftbehind.core.data.repository.PreferenceRepository
import com.toadfrog.nocalorieleftbehind.core.domain.ValidateFoodNameUseCase
import com.toadfrog.nocalorieleftbehind.core.domain.ValidateNutrientAmountUseCase
import com.toadfrog.nocalorieleftbehind.core.domain.model.Food
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientDto
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.toadfrog.nocalorieleftbehind.core.ui.toNutrition
import com.toadfrog.nocalorieleftbehind.core.ui.toNutritionUi
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.core.util.Utils
import com.toadfrog.nocalorieleftbehind.logfood.data.toLogFoodUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, FormatStringsInDatetimeFormats::class)
class LogFoodViewModel(
    private val foodRepository: FoodRepository,
    private val preferenceRepository: PreferenceRepository,
    private val validateNutrientAmountUseCase: ValidateNutrientAmountUseCase,
    private val validateFoodNameUseCase: ValidateFoodNameUseCase
) : ViewModel() {

    fun initialize(foodId: Long?) {
        viewModelScope.launch {
            when {
                foodId == null -> {
                    val preference = preferenceRepository.getPreference().first()

                    if (preference == null) return@launch _logFoodUiFlow.update {
                        it.copy(
                            state = State.Error,
                            errorMessage = R.string.try_refreshing
                        )
                    }

                    _logFoodUiFlow.update {
                        LogFoodUi(
                            state = State.Success,
                            nutritionUi = preference.nutrition.toNutritionUi(copyData = false)
                        )
                    }
                }

                else -> {
                    val logFoodUi = foodRepository.getFoodWithId(foodId).toLogFoodUi()
                    _logFoodUiFlow.update { logFoodUi }
                }
            }
        }
    }

    private val _logFoodUiFlow = MutableStateFlow<LogFoodUi>(LogFoodUi())
    val logFoodUiFLow = _logFoodUiFlow.asStateFlow()

    fun onFoodName(name: String) {
        viewModelScope.launch {
            _logFoodUiFlow.update { logFoodUi ->
                logFoodUi.copy(nameUiState = validateFoodNameUseCase(name))
            }
        }
    }

    fun onNutrientInput(nutrientDto: NutrientDto) {
        viewModelScope.launch {
            val nutrientUiState = validateNutrientAmountUseCase(nutrientDto = nutrientDto)
            _logFoodUiFlow.update { logFoodUiState ->
                logFoodUiState.copy(
                    nutritionUi = logFoodUiState.nutritionUi.updateNutrient(
                        nutrient = nutrientDto.nutrient,
                        nutrientUiState = nutrientUiState
                    )
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun logFood(onCompletion: () -> Unit = {}) {
        viewModelScope.launch {
            val currentLogFoodUi = _logFoodUiFlow.value

            _logFoodUiFlow.update { it.copy(state = State.Loading) }

            val logFoodUi = currentLogFoodUi.let {
                LogFoodUi(
                    id = it.id,
                    nameUiState = validateFoodNameUseCase(name = it.nameUiState.name),
                    nutritionUi = NutritionUiState(
                        calories = validateNutrientAmountUseCase(nutrientUiState = it.nutritionUi.calories),
                        protein = it.nutritionUi.protein?.let { nutrientUiState ->
                            validateNutrientAmountUseCase(nutrientUiState = nutrientUiState)
                        },
                        carbs = it.nutritionUi.carbs?.let { nutrientUiState ->
                            validateNutrientAmountUseCase(nutrientUiState = nutrientUiState)
                        },
                        fats = it.nutritionUi.fats?.let { nutrientUiState ->
                            validateNutrientAmountUseCase(nutrientUiState = nutrientUiState)
                        }
                    )
                )
            }

            if (!logFoodUi.isLogFoodUiValid()) {
                _logFoodUiFlow.update { logFoodUi.copy(state = State.Success) }
                return@launch
            }

            val food = Food(
                id = logFoodUi.id ?: 0,
                name = logFoodUi.nameUiState.name,
                nutrition = logFoodUi.nutritionUi.toNutrition(),
                timeStampEpochSec = Utils.todayMidnightTimestamp()
            )

            foodRepository.updateFood(food)

            onCompletion()
        }
    }

}