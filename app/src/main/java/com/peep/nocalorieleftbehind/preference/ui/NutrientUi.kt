package com.peep.nocalorieleftbehind.preference.ui

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.util.UiElement

@Immutable
data class NutrientUi(
    val nutrient: Nutrient,
    val ui: UiElement<String>
)