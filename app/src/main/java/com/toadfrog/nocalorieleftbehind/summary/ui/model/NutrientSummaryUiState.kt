package com.toadfrog.nocalorieleftbehind.summary.ui.model

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient

@Immutable
@Keep
data class NutrientSummaryUiState(
    val nutrient: Nutrient,
    val eaten: Int,
    val left: Int,
    val total: Int
)
