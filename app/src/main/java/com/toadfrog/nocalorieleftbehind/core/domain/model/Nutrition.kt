package com.toadfrog.nocalorieleftbehind.core.domain.model

import androidx.annotation.Keep
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientDto
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

    fun getMacronutrients() = buildList<NutrientDto> {
        protein?.let { add(NutrientDto(nutrient = Nutrient.PROTEIN, amount = it.toString())) }
        carbs?.let { add(NutrientDto(nutrient = Nutrient.CARBS, amount = it.toString())) }
        fats?.let { add(NutrientDto(nutrient = Nutrient.FATS, amount = it.toString())) }
    }

}