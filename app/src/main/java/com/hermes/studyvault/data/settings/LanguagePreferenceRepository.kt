package com.hermes.studyvault.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hermes.studyvault.domain.settings.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(name = "language_settings")

class LanguagePreferenceRepository(
    private val dataStore: DataStore<Preferences>,
) {
    val selectedLanguage: Flow<AppLanguage> = dataStore.data.map { preferences ->
        AppLanguage.fromCode(preferences[LanguageCodeKey])
    }

    suspend fun saveLanguage(language: AppLanguage) {
        dataStore.edit { preferences ->
            preferences[LanguageCodeKey] = language.code
        }
    }

    private companion object {
        val LanguageCodeKey = stringPreferencesKey("language_code")
    }
}
