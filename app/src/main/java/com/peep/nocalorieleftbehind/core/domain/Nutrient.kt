package com.peep.nocalorieleftbehind.core.domain

import com.peep.nocalorieleftbehind.R

enum class Nutrient(val nameResId: Int, val iconResId: Int, val unit: String) {
    CALORIES(nameResId = R.string.calories, iconResId = R.drawable.outline_mode_heat_24, unit = "cal"),
    PROTEIN(nameResId = R.string.protein, iconResId = R.drawable.outline_meat_24, unit = "g"),
    FATS(nameResId = R.string.fats, iconResId = R.drawable.outline_oil_24, "g"),
    CARBS(nameResId = R.string.carbs, iconResId = R.drawable.outline_bread_24, unit = "g");
}

fun nutrientList(includeCalories: Boolean = false) = buildList<Nutrient> {
    if (includeCalories) add(Nutrient.CALORIES)
    add(Nutrient.PROTEIN)
    add(Nutrient.FATS)
    add(Nutrient.CARBS)
}