package com.toadfrog.nocalorieleftbehind.core.domain

import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.ui.model.NameUiState
import com.toadfrog.nocalorieleftbehind.core.util.State

class ValidateFoodNameUseCase() {

    operator fun invoke(name: String): NameUiState {
        if (name.isBlank()) return NameUiState(
            state = State.Error,
            name = name,
            errorMessage = R.string.enter_a_name
        )

        return NameUiState(
            state = State.Success,
            name = name.trim(),
            errorMessage = null
        )
    }
}