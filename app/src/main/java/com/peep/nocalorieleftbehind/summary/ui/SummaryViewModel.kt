package com.peep.nocalorieleftbehind.summary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.peep.nocalorieleftbehind.core.util.Ui
import com.peep.nocalorieleftbehind.core.util.Utils
import com.peep.nocalorieleftbehind.logfood.data.FoodRepository
import com.peep.nocalorieleftbehind.preference.data.PreferenceRepository
import com.peep.nocalorieleftbehind.summary.data.toFoodUi
import com.peep.nocalorieleftbehind.summary.data.toSummaryUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SummaryViewModel(
    private val foodRepository: FoodRepository,
    preferenceRepository: PreferenceRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            foodRepository
                .getFoodsOnDay(timeStampEpochSec = Utils.todayMidnightTimestamp())
                .combineTransform(
                    flow = preferenceRepository.getPreference()
                ) { foodFromToday, preferences ->
                    if (preferences == null) throw NullPointerException()

                    emit(toSummaryUi(foodsEaten = foodFromToday, preferences = preferences))
                }.catch {
                    _ui.update { Ui.Error }
                }.collect { summaryUiState ->
                    _summaryUiState.update { summaryUiState }
                    _ui.update { Ui.Success }
                }
        }
    }

    private val _ui: MutableStateFlow<Ui> = MutableStateFlow(Ui.Loading)
    val ui = _ui.asStateFlow()

    private val _summaryUiState: MutableStateFlow<SummaryUiState> = MutableStateFlow(SummaryUiState.default)
    val summaryUiState: StateFlow<SummaryUiState> = _summaryUiState.asStateFlow()

    fun deleteFood(id: Long) {
        viewModelScope.launch {
            foodRepository.deleteFood(id)
        }
    }

    val foodEatenPagingData = foodRepository.recentFoods(viewModelScope).map { pagingData ->
        pagingData.map { food ->
            food.toFoodUi()
        }
    }
}