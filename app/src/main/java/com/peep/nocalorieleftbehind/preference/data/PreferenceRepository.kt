package com.peep.nocalorieleftbehind.preference.data

import com.peep.nocalorieleftbehind.core.domain.model.Preferences
import com.peep.nocalorieleftbehind.core.util.Result
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    suspend fun savePreference(preferences: Preferences)

    fun getPreference(): Flow<Preferences?>

}