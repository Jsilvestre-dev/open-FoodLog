package com.toadfrog.nocalorieleftbehind.summary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.core.util.Utils
import com.toadfrog.nocalorieleftbehind.core.data.repository.FoodRepository
import com.toadfrog.nocalorieleftbehind.core.data.repository.PreferenceRepository
import com.toadfrog.nocalorieleftbehind.summary.data.toFoodUi
import com.toadfrog.nocalorieleftbehind.summary.data.toSummaryUi
import com.toadfrog.nocalorieleftbehind.summary.ui.model.SummaryUiState
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
                .getFoodsByTime(timeStampEpochSec = Utils.todayMidnightTimestamp())
                .combineTransform(
                    flow = preferenceRepository.getPreference()
                ) { foodFromToday, preferences ->
                    if (preferences == null) throw NullPointerException()

                    emit(toSummaryUi(foodsEaten = foodFromToday, preference = preferences))
                }.catch {
                    _summaryUiState.update { it.copy(state = State.Error, errorMessage = R.string.try_refreshing) }
                }.collect { summaryUiState ->
                    _summaryUiState.update { summaryUiState }
                }
        }
    }

    private val _summaryUiState: MutableStateFlow<SummaryUiState> = MutableStateFlow(SummaryUiState.default)
    val summaryUiState: StateFlow<SummaryUiState> = _summaryUiState.asStateFlow()

    fun deleteFood(id: Long) {
        viewModelScope.launch {
            foodRepository.removeFoodWithId(id)
        }
    }

    val recentFoodsPagingData =
        foodRepository
            .recentFoods(
                timeStampEpochSec = Utils.todayMidnightTimestamp()
            )
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map { food ->
                    food.toFoodUi()
                }
            }
}