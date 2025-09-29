package com.peep.nocalorieleftbehind.core.domain

import com.peep.nocalorieleftbehind.R
import com.peep.nocalorieleftbehind.core.util.UiElement

class ValidateNutrientUseCase {

    operator fun invoke(nutrientUi: UiElement<String>): UiElement<String> {
        return when (nutrientUi) {
            is UiElement.Error -> nutrientUi
            is UiElement.Loading -> UiElement.Error(messageRes = R.string.enter_an_amount)
            is UiElement.Success<String> -> uiStateResult(nutrientUi.data)
        }
    }

    operator fun invoke(amount: String): UiElement<String> {
        return uiStateResult(amount)
    }

    private fun uiStateResult(amount: String): UiElement<String> {
        if (amount.isBlank()) return UiElement.Error(messageRes = R.string.enter_an_amount)

        val amountInt = amount.trim().toIntOrNull() ?: return UiElement.Error(messageRes = R.string.amount_must_be_num)

        return when {
            amountInt == null -> UiElement.Error(messageRes = R.string.enter_an_amount)
            amountInt < 0 -> UiElement.Error(messageRes = R.string.positive_amount_only)
            else -> UiElement.Success<String>(amountInt.toString())
        }
    }

}