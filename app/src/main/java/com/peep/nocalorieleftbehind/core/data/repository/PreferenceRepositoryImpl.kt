package com.peep.nocalorieleftbehind.core.data.repository

import com.peep.nocalorieleftbehind.core.data.local.NutritionLogLocalDataSource
import com.peep.nocalorieleftbehind.core.data.mapper.toPreference
import com.peep.nocalorieleftbehind.core.data.mapper.toPreferenceEntity
import com.peep.nocalorieleftbehind.core.domain.model.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PreferenceRepositoryImpl(
    private val nutritionLogLocalDataSource: NutritionLogLocalDataSource
) : PreferenceRepository {

    override suspend fun updatePreference(preference: Preference) {
        nutritionLogLocalDataSource.upsertPreferenceEntity(preferenceEntity = preference.toPreferenceEntity())
    }

    override fun getPreference(): Flow<Preference?> =
        nutritionLogLocalDataSource
            .readPreferenceEntity()
            .map { preferenceEntity ->
                preferenceEntity?.toPreference()
            }

}