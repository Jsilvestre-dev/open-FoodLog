package com.peep.nocalorieleftbehind.core.data.repository

import androidx.paging.PagingData
import com.peep.nocalorieleftbehind.core.domain.model.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    suspend fun updateFood(food: Food)

    suspend fun removeFoodWithId(id: Long)

    suspend fun getFoodWithId(id: Long): Food

    fun getFoodsByTime(timeStampEpochSec: Long): Flow<List<Food>>

    fun recentFoods(viewModelScope: CoroutineScope): Flow<PagingData<Food>>
}