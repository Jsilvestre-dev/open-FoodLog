package com.toadfrog.nocalorieleftbehind.core.ui.model

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient


@Immutable
@Keep
data class NutrientInput(
    val amount: String,
    val nutrient: Nutrient,
)