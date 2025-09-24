package com.peep.nocalorieleftbehind.core.domain

import com.peep.nocalorieleftbehind.R
import com.peep.nocalorieleftbehind.core.util.UiState

class ValidateFoodNameUseCase() {

    operator fun invoke(text: String?): UiState<String> {
        return when {
            text.isNullOrBlank() -> UiState.Error(messageRes = R.string.enter_a_name)
            else -> UiState.Success(text)
        }
    }
}