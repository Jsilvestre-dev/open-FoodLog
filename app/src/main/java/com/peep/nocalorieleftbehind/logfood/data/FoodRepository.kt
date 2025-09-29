package com.peep.nocalorieleftbehind.logfood.data

import androidx.paging.PagingData
import com.peep.nocalorieleftbehind.core.domain.model.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    suspend fun saveFood(food: Food)

    suspend fun deleteFood(id: Long)

    suspend fun getFoodWithId(id: Long): Food

    fun getFoodsOnDay(timeStampEpochSec: Long): Flow<List<Food>>

    fun recentFoods(viewModelScope: CoroutineScope): Flow<PagingData<Food>>
}