package com.toadfrog.nocalorieleftbehind.core.data.local

import androidx.paging.Pager
import com.toadfrog.nocalorieleftbehind.core.data.local.entity.FoodEntity
import com.toadfrog.nocalorieleftbehind.core.data.local.entity.PreferenceEntity
import kotlinx.coroutines.flow.Flow

interface NutritionLogLocalDataSource {
    suspend fun upsertPreferenceEntity(preferenceEntity: PreferenceEntity)

    fun readPreferenceEntity(): Flow<PreferenceEntity?>

    suspend fun upsertFoodEntity(foodEntity: FoodEntity)

    suspend fun deleteFoodEntityById(id: Long)

    suspend fun readFoodEntityById(id: Long): FoodEntity

    fun readFoodEntitiesByTime(timeStampEpochSec: Long): Flow<List<FoodEntity>?>

    fun readRecentFoodEntitiesPager(): Pager<Int, FoodEntity>
}