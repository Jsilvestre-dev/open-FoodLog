package com.peep.nocalorieleftbehind.core.data.repository

import com.peep.nocalorieleftbehind.core.domain.model.Preference
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    suspend fun updatePreference(preference: Preference)

    fun getPreference(): Flow<Preference?>

}