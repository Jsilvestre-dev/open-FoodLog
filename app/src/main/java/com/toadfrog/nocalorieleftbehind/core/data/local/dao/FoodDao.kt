package com.toadfrog.nocalorieleftbehind.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.toadfrog.nocalorieleftbehind.core.data.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Upsert
    suspend fun upsertFoodEntity(foodEntity: FoodEntity): Long

    @Query("DELETE FROM FoodEntity WHERE id = :id")
    suspend fun deleteFoodEntityById(id: Long): Int


    @Query("SELECT * FROM FoodEntity WHERE id = :foodId")
    suspend fun readFoodEntityById(foodId: Long): FoodEntity

    @Query("SELECT * FROM FoodEntity WHERE timeStampEpochSec = :timeStampEpochSec ")
    fun readFoodEntitiesByTime(timeStampEpochSec: Long): Flow<List<FoodEntity>>

    @Query("SELECT * FROM FoodEntity ORDER BY id DESC")
    fun readRecentFoodPagingSource(): PagingSource<Int, FoodEntity>

}