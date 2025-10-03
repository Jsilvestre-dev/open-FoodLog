package com.toadfrog.nocalorieleftbehind.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.toadfrog.nocalorieleftbehind.core.data.local.entity.PreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferenceDao {

    @Upsert
    suspend fun upsertPreferenceEntity(preferenceEntity: PreferenceEntity)

    @Query("SELECT * FROM PreferenceEntity")
    fun readPreferenceEntity(): Flow<PreferenceEntity?>

}