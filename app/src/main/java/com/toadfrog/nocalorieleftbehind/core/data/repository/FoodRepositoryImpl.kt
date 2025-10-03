package com.toadfrog.nocalorieleftbehind.core.data.repository

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.toadfrog.nocalorieleftbehind.core.data.local.NutritionLogLocalDataSource
import com.toadfrog.nocalorieleftbehind.core.data.mapper.toFood
import com.toadfrog.nocalorieleftbehind.core.data.mapper.toFoodEntity
import com.toadfrog.nocalorieleftbehind.core.domain.model.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepositoryImpl(
    private val localDataSource: NutritionLogLocalDataSource
) : FoodRepository {

    override suspend fun updateFood(food: Food) {
        localDataSource.upsertFoodEntity(food.toFoodEntity())
    }

    override suspend fun removeFoodWithId(id: Long) {
        localDataSource.deleteFoodEntityById(id)
    }

    override suspend fun getFoodWithId(id: Long): Food {
        return localDataSource.readFoodEntityById(id).toFood()
    }

    override fun getFoodsByTime(timeStampEpochSec: Long): Flow<List<Food>> {
        return localDataSource
            .readFoodEntitiesByTime(timeStampEpochSec)
            .map { foodEntityList ->
                foodEntityList?.map { foodEntity ->
                    foodEntity.toFood()
                } ?: emptyList()
            }
    }

    override fun recentFoods(viewModelScope: CoroutineScope): Flow<PagingData<Food>> {
        return localDataSource.readRecentFoodEntitiesPager().flow
            .cachedIn(viewModelScope).map { pagingData ->
                pagingData.map { foodEntity ->
                    foodEntity.toFood()
                }
            }
    }
}