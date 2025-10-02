package com.peep.nocalorieleftbehind.core.ui.model

import androidx.compose.runtime.Immutable
import com.peep.nocalorieleftbehind.core.util.State

@Immutable
data class NameUiState(
    val state: State = State.Loading,
    val name: String = "",
    val errorMessage: Int? = null
)