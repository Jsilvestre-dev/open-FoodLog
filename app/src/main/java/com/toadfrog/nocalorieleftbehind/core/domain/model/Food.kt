package com.toadfrog.nocalorieleftbehind.core.domain.model

import androidx.annotation.Keep

@Keep
data class Food(
    val id: Long,
    val name: String,
    val nutrition: Nutrition,
    val timeStampEpochSec: Long
)