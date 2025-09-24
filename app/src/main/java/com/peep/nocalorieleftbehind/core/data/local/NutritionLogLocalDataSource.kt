package com.peep.nocalorieleftbehind.core.data.local

import androidx.paging.Pager
import com.peep.nocalorieleftbehind.core.data.local.entity.FoodEntity
import com.peep.nocalorieleftbehind.core.data.local.entity.PreferencesEntity
import com.peep.nocalorieleftbehind.core.util.Result
import kotlinx.coroutines.flow.Flow

interface NutritionLogLocalDataSource {
    suspend fun upsertPreference(preferencesEntity: PreferencesEntity)

    fun queryPreference(): Flow<PreferencesEntity?>

    suspend fun upsertFood(foodEntity: FoodEntity): Result

    suspend fun deleteFoodEntityById(id: Long): Result

    suspend fun findFoodById(id: Long): FoodEntity

    fun getFoodByTimestamp(timeStampEpochSec: Long): Flow<List<FoodEntity>?>

    fun recentFoodsPager(): Pager<Int, FoodEntity>
}