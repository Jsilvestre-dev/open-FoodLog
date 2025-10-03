package com.toadfrog.nocalorieleftbehind.core.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.toadfrog.nocalorieleftbehind.core.domain.model.Nutrition

@Entity
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    @Embedded
    val nutrition: Nutrition,
    val timeStampEpochSec: Long
)