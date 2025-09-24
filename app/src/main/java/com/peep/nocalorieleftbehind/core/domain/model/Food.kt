package com.peep.nocalorieleftbehind.core.domain.model

data class Food(
    val id: Long,
    val name: String,
    val nutrition: Nutrition,
    val timeStampEpochSec: Long
)