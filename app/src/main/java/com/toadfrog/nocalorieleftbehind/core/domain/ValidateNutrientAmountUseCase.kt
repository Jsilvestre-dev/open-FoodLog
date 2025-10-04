package com.toadfrog.nocalorieleftbehind.core.domain

import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientUiState
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientInput

class ValidateNutrientAmountUseCase {

    operator fun invoke(nutrientInput: NutrientInput): NutrientUiState {

        val (uiState, errorMessage) = validateAmount(nutrientInput.amount)

        return NutrientUiState(
            nutrient = nutrientInput.nutrient,
            state = uiState,
            errorMessage = errorMessage,
            data = nutrientInput.amount
        )
    }

    operator fun invoke(nutrientUiState: NutrientUiState): NutrientUiState {

        val (uiState, errorMessage) = validateAmount(nutrientUiState.data)

        return nutrientUiState.copy(
            state = uiState,
            errorMessage = errorMessage
        )
    }

    private fun validateAmount(amount: String): Pair<State, Int?> {
        if (amount.isBlank()) return State.Error to R.string.enter_an_amount

        val amountInt = amount.trim().toIntOrNull() ?: return State.Error to R.string.amount_must_be_num

        if (amountInt < 0) return State.Error to R.string.positive_amount_only

        return State.Success to null
    }
}