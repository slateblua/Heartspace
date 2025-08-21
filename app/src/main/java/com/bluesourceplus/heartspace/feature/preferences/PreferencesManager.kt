package com.bluesourceplus.heartspace.feature.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

enum class AppTheme(val value: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system");

    companion object {
        fun fromValue(value: String?): AppTheme {
            return entries.find { it.value == value } ?: SYSTEM
        }
    }
}

object PreferencesKeys {
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    val SELECTED_THEME = stringPreferencesKey("selected_theme")
}

class PreferencesManager(private val context: Context) {

    val onboardingCompleted = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { it[PreferencesKeys.ONBOARDING_COMPLETED] = true }
    }

    // Theme
    val selectedTheme = context.dataStore.data
        .map { preferences -> AppTheme.fromValue(preferences[PreferencesKeys.SELECTED_THEME]) }

    suspend fun setSelectedTheme(theme: AppTheme) {
        context.dataStore.edit { it[PreferencesKeys.SELECTED_THEME] = theme.value }
    }
}
