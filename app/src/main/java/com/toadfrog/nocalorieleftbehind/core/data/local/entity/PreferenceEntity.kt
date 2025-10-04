package com.toadfrog.nocalorieleftbehind.core.data.local.entity

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.toadfrog.nocalorieleftbehind.core.domain.model.Nutrition
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Entity
@Serializable
@Keep
@OptIn(ExperimentalSerializationApi::class)
data class PreferenceEntity(
    @PrimaryKey
    @EncodeDefault
    val id: Int = 1,
    @Embedded
    val nutrition: Nutrition
)