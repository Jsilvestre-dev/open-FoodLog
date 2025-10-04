package com.toadfrog.nocalorieleftbehind.core.domain.model

import androidx.annotation.Keep
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class Nutrition(
    val calories: Int,
    val protein: Int?,
    val carbs: Int?,
    val fats: Int?
) {
    fun updateNutrient(nutrient: Nutrient, value: Int) = when (nutrient) {
        Nutrient.CALORIES -> copy(calories = value)
        Nutrient.PROTEIN -> copy(protein = value)
        Nutrient.FATS -> copy(fats = value)
        Nutrient.CARBS -> copy(carbs = value)
    }
}