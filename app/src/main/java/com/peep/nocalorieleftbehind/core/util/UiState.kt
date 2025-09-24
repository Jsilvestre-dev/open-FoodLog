package com.peep.nocalorieleftbehind.core.util

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(
        val internalMessage: String = "",
        val messageRes: Int? = null,
        val iconRes: Int? = null
    ) : UiState<Nothing>
}