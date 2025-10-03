package com.toadfrog.nocalorieleftbehind.core.data.mapper

import com.toadfrog.nocalorieleftbehind.core.data.local.entity.FoodEntity
import com.toadfrog.nocalorieleftbehind.core.data.local.entity.PreferenceEntity
import com.toadfrog.nocalorieleftbehind.core.domain.model.Food
import com.toadfrog.nocalorieleftbehind.core.domain.model.Preference

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
