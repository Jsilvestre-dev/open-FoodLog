package com.peep.nocalorieleftbehind.preference.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.domain.Nutrient

@Immutable
data class NutrientData(
    val value: String,
    val nutrient: Nutrient,
)