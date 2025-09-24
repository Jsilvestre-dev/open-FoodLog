package com.peep.nocalorieleftbehind.logfood.data

import androidx.paging.PagingData
import com.peep.nocalorieleftbehind.core.domain.model.Food
import com.peep.nocalorieleftbehind.core.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    suspend fun saveFood(food: Food): Result

    suspend fun deleteFood(id: Long): Result

    suspend fun getFoodWithId(id: Long): Food

    fun getTodayFoods(timeStampEpochSec: Long): Flow<List<Food>>

    fun recentFoods(viewModelScope: CoroutineScope): Flow<PagingData<Food>>
}