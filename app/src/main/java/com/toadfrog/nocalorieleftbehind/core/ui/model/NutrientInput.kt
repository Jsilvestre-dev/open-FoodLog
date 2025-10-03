package com.toadfrog.nocalorieleftbehind.core.ui.model

import androidx.compose.runtime.Immutable
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient

@Immutable
data class NutrientInput(
    val amount: String,
    val nutrient: Nutrient,
)