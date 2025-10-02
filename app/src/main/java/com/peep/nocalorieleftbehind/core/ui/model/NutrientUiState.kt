package com.peep.nocalorieleftbehind.core.ui.model

import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.util.State

data class NutrientUiState(
    val state: State = State.Loading,
    val nutrient: Nutrient,
    val data: String = "",
    val errorMessage: Int? = null
)