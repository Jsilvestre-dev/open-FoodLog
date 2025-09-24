package com.peep.nocalorieleftbehind.core.data.mapper

import com.peep.nocalorieleftbehind.core.data.local.entity.FoodEntity
import com.peep.nocalorieleftbehind.core.data.local.entity.PreferencesEntity
import com.peep.nocalorieleftbehind.core.domain.model.Food
import com.peep.nocalorieleftbehind.core.domain.model.Preferences

fun Preferences.toPreferenceEntity() = PreferencesEntity(
    nutrition = nutrition
)

fun PreferencesEntity.toPreference(): Preferences = Preferences(
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
