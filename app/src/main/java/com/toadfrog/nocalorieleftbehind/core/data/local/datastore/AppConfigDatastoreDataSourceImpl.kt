package com.toadfrog.nocalorieleftbehind.core.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class AppConfigDatastoreDataSource(
    private val context: Context
) {
    private val isOnboardingCompleted = booleanPreferencesKey(ONBOARDING_COMPLETED)
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

    suspend fun isOnboardingCompleted(): Boolean {
        return withContext(Dispatchers.IO) {
            context.dataStore.data.first()
                .let { preferences ->
                    preferences[isOnboardingCompleted] ?: false
                }
        }
    }

    suspend fun onboardingCompleted() {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[isOnboardingCompleted] = true
            }
        }
    }

    companion object {
        const val DATASTORE_NAME = "app_config"
        private const val ONBOARDING_COMPLETED = "onboarding_completed"
    }
}