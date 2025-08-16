package com.mika.pea.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepository(private val store: DataStore<Preferences>) {
    private val KEY_API = stringPreferencesKey("alpha_vantage_api_key")

    val apiKey: Flow<String> = store.data.map { it[KEY_API] ?: "" }

    suspend fun setApiKey(key: String) {
        store.edit { prefs ->
            prefs[KEY_API] = key
        }
    }
}
