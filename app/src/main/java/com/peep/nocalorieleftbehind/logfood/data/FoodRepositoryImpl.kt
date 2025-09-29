package com.peep.nocalorieleftbehind.logfood.data

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.peep.nocalorieleftbehind.core.data.local.NutritionLogLocalDataSource
import com.peep.nocalorieleftbehind.core.data.mapper.toFood
import com.peep.nocalorieleftbehind.core.data.mapper.toFoodEntity
import com.peep.nocalorieleftbehind.core.domain.model.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepositoryImpl(
    private val localDataSource: NutritionLogLocalDataSource
) : FoodRepository {

    override suspend fun saveFood(food: Food) {
        localDataSource.upsertFood(food.toFoodEntity())
    }

    override suspend fun deleteFood(id: Long) {
        localDataSource.deleteFoodEntityById(id)
    }

    override suspend fun getFoodWithId(id: Long): Food {
        return localDataSource.findFoodById(id).toFood()
    }

    override fun getFoodsOnDay(timeStampEpochSec: Long): Flow<List<Food>> {
        return localDataSource.getFoodByTimestamp(timeStampEpochSec).map { foodEntityList ->
            foodEntityList?.map { foodEntity ->
                foodEntity.toFood()
            } ?: emptyList()
        }
    }

    override fun recentFoods(viewModelScope: CoroutineScope): Flow<PagingData<Food>> {
        return localDataSource.recentFoodsPager().flow
            .cachedIn(viewModelScope).map { pagingData ->
                pagingData.map { foodEntity ->
                    foodEntity.toFood()
                }
            }
    }
}