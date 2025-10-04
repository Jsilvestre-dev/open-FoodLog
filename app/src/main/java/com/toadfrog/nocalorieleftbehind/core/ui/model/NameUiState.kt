package com.toadfrog.nocalorieleftbehind.core.ui.model

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import com.toadfrog.nocalorieleftbehind.core.util.State

@Immutable
@Keep
data class NameUiState(
    val state: State = State.Loading,
    val name: String = "",
    val errorMessage: Int? = null
)