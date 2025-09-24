package com.peep.nocalorieleftbehind.core.domain

import com.peep.nocalorieleftbehind.R
import com.peep.nocalorieleftbehind.core.util.UiState

class ValidateNutrientUseCase {

    operator fun invoke(nutrientUi: UiState<String>): UiState<String> {
        return when (nutrientUi) {
            is UiState.Error -> nutrientUi
            is UiState.Loading -> UiState.Error(messageRes = R.string.enter_an_amount)
            is UiState.Success<String> -> uiStateResult(nutrientUi.data)
        }
    }

    operator fun invoke(amount: String): UiState<String> {
        return uiStateResult(amount)
    }

    private fun uiStateResult(amount: String): UiState<String> {
        if (amount.isBlank()) return UiState.Error(messageRes = R.string.enter_an_amount)

        val amountInt = amount.trim().toIntOrNull() ?: return UiState.Error(messageRes = R.string.amount_must_be_num)

        return when {
            amountInt == null -> UiState.Error(messageRes = R.string.enter_an_amount)
            amountInt < 0 -> UiState.Error(messageRes = R.string.positive_amount_only)
            else -> UiState.Success<String>(amountInt.toString())
        }
    }

}