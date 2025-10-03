package com.toadfrog.nocalorieleftbehind.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.toadfrog.nocalorieleftbehind.core.data.local.dao.FoodDao
import com.toadfrog.nocalorieleftbehind.core.data.local.dao.PreferenceDao
import com.toadfrog.nocalorieleftbehind.core.data.local.entity.FoodEntity
import com.toadfrog.nocalorieleftbehind.core.data.local.entity.PreferenceEntity

@Database(
    entities = [PreferenceEntity::class, FoodEntity::class],
    version = 1,
    exportSchema = true
)
abstract class NutritionLogDatabase : RoomDatabase() {
    abstract fun preferenceDao(): PreferenceDao

    abstract fun foodDao(): FoodDao
}