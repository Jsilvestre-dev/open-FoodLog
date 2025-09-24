package com.peep.nocalorieleftbehind.preference.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.util.UiState

@Immutable
data class NutrientUi(
    val nutrient: Nutrient,
    val ui: UiState<String>
)