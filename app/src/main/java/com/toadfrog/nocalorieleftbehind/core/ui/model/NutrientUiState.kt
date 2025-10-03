package com.toadfrog.nocalorieleftbehind.core.ui.model

import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.util.State

data class NutrientUiState(
    val state: State = State.Loading,
    val nutrient: Nutrient,
    val data: String = "",
    val errorMessage: Int? = null
)