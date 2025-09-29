package com.peep.nocalorieleftbehind.preference.data

import com.peep.nocalorieleftbehind.core.data.local.NutritionLogLocalDataSource
import com.peep.nocalorieleftbehind.core.data.mapper.toPreference
import com.peep.nocalorieleftbehind.core.data.mapper.toPreferenceEntity
import com.peep.nocalorieleftbehind.core.domain.model.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PreferenceRepositoryImpl(
    private val nutritionLogLocalDataSource: NutritionLogLocalDataSource
) : PreferenceRepository {

    override suspend fun savePreference(preferences: Preferences) {
        nutritionLogLocalDataSource.upsertPreference(preferences.toPreferenceEntity())
    }

    override fun getPreference(): Flow<Preferences?> =
        nutritionLogLocalDataSource
            .queryPreference()
            .map { preferenceEntity ->
                preferenceEntity?.toPreference()
            }

}