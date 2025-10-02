package com.peep.nocalorieleftbehind.core.data.mapper

import com.peep.nocalorieleftbehind.core.data.local.entity.FoodEntity
import com.peep.nocalorieleftbehind.core.data.local.entity.PreferenceEntity
import com.peep.nocalorieleftbehind.core.domain.model.Food
import com.peep.nocalorieleftbehind.core.domain.model.Preference

fun Preference.toPreferenceEntity() = PreferenceEntity(
    nutrition = nutrition
)

fun PreferenceEntity.toPreference(): Preference = Preference(
    nutrition = nutrition
)

fun Food.toFoodEntity() = FoodEntity(
    id = 0,
    name = name,
    nutrition = nutrition,
    timeStampEpochSec = timeStampEpochSec
)

fun FoodEntity.toFood() = Food(
    id = id,
    name = name,
    nutrition = nutrition,
    timeStampEpochSec = timeStampEpochSec
)
