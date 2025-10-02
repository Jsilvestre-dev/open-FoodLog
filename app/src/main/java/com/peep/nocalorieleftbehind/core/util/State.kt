package com.peep.nocalorieleftbehind.core.util

sealed interface State {
    object Success: State
    object Loading : State
    object Error : State
}

