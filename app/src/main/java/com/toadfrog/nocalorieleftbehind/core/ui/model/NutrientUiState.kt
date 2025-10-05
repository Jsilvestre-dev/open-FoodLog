package com.toadfrog.nocalorieleftbehind.core.ui.model

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.util.State

@Immutable
@Keep
data class NutrientUiState(
    val state: State = State.Loading,
    val nutrient: Nutrient,
    val data: String,
    val errorMessage: Int? = null
)