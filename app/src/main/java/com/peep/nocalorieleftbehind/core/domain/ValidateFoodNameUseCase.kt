package com.peep.nocalorieleftbehind.core.domain

import com.peep.nocalorieleftbehind.R
import com.peep.nocalorieleftbehind.core.util.UiElement

class ValidateFoodNameUseCase() {

    operator fun invoke(text: String?): UiElement<String> {
        return when {
            text.isNullOrBlank() -> UiElement.Error(messageRes = R.string.enter_a_name)
            else -> UiElement.Success(text)
        }
    }
}