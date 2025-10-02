package com.peep.nocalorieleftbehind.summary.ui.model

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.domain.model.Nutrition

@Immutable
data class FoodUi(
    val id: Long,
    val name: String,
    val nutrition: Nutrition,
    val timeStampEpochSec: Long
)