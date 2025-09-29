package com.peep.nocalorieleftbehind.logfood.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peep.nocalorieleftbehind.core.domain.ValidateFoodNameUseCase
import com.peep.nocalorieleftbehind.core.domain.ValidateNutrientUseCase
import com.peep.nocalorieleftbehind.core.domain.model.Food
import com.peep.nocalorieleftbehind.core.ui.NutritionUi
import com.peep.nocalorieleftbehind.core.ui.toNutrition
import com.peep.nocalorieleftbehind.core.ui.toNutritionUiLoading
import com.peep.nocalorieleftbehind.core.util.Ui
import com.peep.nocalorieleftbehind.core.util.UiElement
import com.peep.nocalorieleftbehind.core.util.Utils
import com.peep.nocalorieleftbehind.logfood.data.FoodRepository
import com.peep.nocalorieleftbehind.logfood.data.toLogFoodUi
import com.peep.nocalorieleftbehind.preference.data.PreferenceRepository
import com.peep.nocalorieleftbehind.preference.ui.NutrientData
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
    private val validateNutrientUseCase: ValidateNutrientUseCase,
    private val validateFoodNameUseCase: ValidateFoodNameUseCase
) : ViewModel() {

    fun initialize(foodId: Long?) {
        viewModelScope.launch {
            when {
                foodId == null -> {
                    val preference = preferenceRepository.getPreference().first()

                    if (preference == null) return@launch _logFoodUiFlow.update { it.copy(uiState = Ui.Error) }

                    _logFoodUiFlow.update {
                        LogFoodUi(
                            uiState = Ui.Success,
                            nutritionUi = preference.nutrition.toNutritionUiLoading()
                        )
                    }
                }

                else -> {
                    val logFoodUi = foodRepository.getFoodWithId(foodId).toLogFoodUi()
                    _logFoodUiFlow.update { logFoodUi.copy(uiState = Ui.Success) }
                }
            }
        }
    }

    private val _logFoodUiFlow = MutableStateFlow<LogFoodUi>(LogFoodUi())
    val logFoodUiFLow = _logFoodUiFlow.asStateFlow()

    fun onFoodName(name: String) {
        viewModelScope.launch {
            _logFoodUiFlow.update { logFoodUi ->
                logFoodUi.copy(foodNameUi = validateFoodNameUseCase(name))
            }
        }
    }

    fun onMacro(nutrientData: NutrientData) {
        viewModelScope.launch {
            val validatedNutrientValue = validateNutrientUseCase(nutrientData.value)
            _logFoodUiFlow.update { logFoodUiState ->

                logFoodUiState.copy(
                    nutritionUi = logFoodUiState.nutritionUi.updateNutrientUi(
                        nutrientData.nutrient,
                        validatedNutrientValue
                    )
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun logFood(onCompletion: () -> Unit = {}) {
        viewModelScope.launch {
            val currentLogFoodUi = _logFoodUiFlow.value

            _logFoodUiFlow.update { it.copy(uiState = Ui.Loading) }

            val logFoodUi = currentLogFoodUi.let { logFoodUi ->
                LogFoodUi(
                    foodNameUi = validateFoodNameUseCase((logFoodUi.foodNameUi as? UiElement.Success)?.data),
                    nutritionUi = NutritionUi(
                        calories = validateNutrientUseCase(
                            (logFoodUi.nutritionUi.calories as? UiElement.Success)?.data ?: ""
                        ),
                        protein = (logFoodUi.nutritionUi.protein as? UiElement.Success)?.data?.let {
                            validateNutrientUseCase(
                                it
                            )
                        },
                        carbs = (logFoodUi.nutritionUi.carbs as? UiElement.Success)?.data?.let {
                            validateNutrientUseCase(
                                it
                            )
                        },
                        fats = (logFoodUi.nutritionUi.fats as? UiElement.Success)?.data?.let {
                            validateNutrientUseCase(
                                it
                            )
                        }
                    )
                )
            }

            if (!logFoodUi.isLogFoodUiValid()) {
                _logFoodUiFlow.update { logFoodUi.copy(uiState = Ui.Success) }
                return@launch
            }

            val food = Food(
                id = 0,
                name = (logFoodUi.foodNameUi as UiElement.Success).data,
                nutrition = logFoodUi.nutritionUi.toNutrition(),
                timeStampEpochSec = Utils.todayMidnightTimestamp()
            )

            foodRepository.saveFood(food)

            onCompletion()
        }
    }

}