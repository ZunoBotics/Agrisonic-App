package com.agrisonic.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "agrisonic_preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        private val SESSION_TOKEN = stringPreferencesKey("session_token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val LANGUAGE = stringPreferencesKey("language")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val FCM_TOKEN = stringPreferencesKey("fcm_token")
    }

    // Session Token
    fun saveSessionToken(token: String) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[SESSION_TOKEN] = token
            }
        }
    }

    fun getSessionToken(): String? {
        return runBlocking {
            context.dataStore.data.first()[SESSION_TOKEN]
        }
    }

    suspend fun clearSessionToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(SESSION_TOKEN)
        }
    }

    // User Info
    suspend fun saveUserInfo(userId: String, email: String, name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_EMAIL] = email
            preferences[USER_NAME] = name
            preferences[IS_LOGGED_IN] = true
        }
    }

    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID]
        }
    }

    fun getUserEmail(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_EMAIL]
        }
    }

    fun getUserName(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_NAME]
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }
    }

    // Language
    suspend fun saveLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = languageCode
        }
    }

    fun getLanguage(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[LANGUAGE] ?: "en"
        }
    }

    // FCM Token
    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[FCM_TOKEN] = token
        }
    }

    fun getFcmToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[FCM_TOKEN]
        }
    }

    // Logout
    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
