package com.peep.nocalorieleftbehind.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.peep.nocalorieleftbehind.core.data.local.entity.PreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferenceDao {

    @Upsert
    suspend fun upsertPreference(intakeTargetsEntity: PreferencesEntity)

    @Query("SELECT * FROM PreferencesEntity")
    fun getPreference(): Flow<PreferencesEntity?>

}