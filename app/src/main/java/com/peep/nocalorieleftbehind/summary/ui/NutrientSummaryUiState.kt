package com.peep.nocalorieleftbehind.summary.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.domain.Nutrient

@Immutable
data class NutrientSummaryUiState(
    val nutrient: Nutrient,
    val eaten: Int,
    val left: Int,
    val total: Int
)
