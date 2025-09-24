package com.peep.nocalorieleftbehind.summary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.peep.nocalorieleftbehind.R
import com.peep.nocalorieleftbehind.core.util.UiState
import com.peep.nocalorieleftbehind.core.util.Utils
import com.peep.nocalorieleftbehind.logfood.data.FoodRepository
import com.peep.nocalorieleftbehind.preference.data.PreferenceRepository
import com.peep.nocalorieleftbehind.summary.data.toFoodUi
import com.peep.nocalorieleftbehind.summary.data.toSummaryUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SummaryViewModel(
    private val foodRepository: FoodRepository,
    preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _screenUiFlow = MutableStateFlow<UiState<*>>(UiState.Loading)
    val screenUiFlow = _screenUiFlow.asStateFlow()

    val summaryUiFlow: StateFlow<SummaryUi> = foodRepository
        .getTodayFoods(Utils.todayMidnightTimestamp())
        .combineTransform(
            preferenceRepository.getPreference()
        ) { foodList, preference ->
            preference
                ?: return@combineTransform _screenUiFlow.update { UiState.Error(messageRes = R.string.check_preferences) }
            emit(toSummaryUi(foodsEaten = foodList, preferences = preference))
            _screenUiFlow.update { UiState.Success(null) }
        }.flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SummaryUi.default
        )

    fun deleteFood(id: Long) {
        viewModelScope.launch {
            foodRepository.deleteFood(id)
        }
    }

    val foodsEatenFlow = foodRepository.recentFoods(viewModelScope).map { pagingData ->
        pagingData.map { food ->
            food.toFoodUi()
        }
    }
}