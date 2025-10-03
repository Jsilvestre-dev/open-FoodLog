package com.toadfrog.nocalorieleftbehind.core.data.repository

import com.toadfrog.nocalorieleftbehind.core.domain.model.Preference
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    suspend fun updatePreference(preference: Preference)

    fun getPreference(): Flow<Preference?>

}