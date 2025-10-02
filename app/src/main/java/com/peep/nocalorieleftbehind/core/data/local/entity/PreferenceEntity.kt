package com.peep.nocalorieleftbehind.core.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peep.nocalorieleftbehind.core.domain.model.Nutrition
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Entity
@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class PreferenceEntity(
    @PrimaryKey
    @EncodeDefault
    val id: Int = 1,
    @Embedded
    val nutrition: Nutrition
)