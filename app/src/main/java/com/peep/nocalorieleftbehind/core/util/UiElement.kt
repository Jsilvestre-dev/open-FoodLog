package com.peep.nocalorieleftbehind.core.util

sealed interface UiElement<out T> {
    object Loading : UiElement<Nothing>
    data class Success<T>(val data: T) : UiElement<T>
    data class Error(
        val internalMessage: String = "",
        val messageRes: Int? = null,
        val iconRes: Int? = null
    ) : UiElement<Nothing>
}