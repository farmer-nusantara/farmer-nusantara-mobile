package com.fahruaz.farmernusantara.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fahruaz.farmernusantara.models.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email!!
            preferences[NAME_KEY] = user.name!!
            preferences[PHONE_KEY] = user.phone!!
            preferences[PASSWORD_KEY] = user.password!!
            preferences[TOKEN_KEY] = ""
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[PHONE_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: ""
            )
        }
    }

    suspend fun login(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email!!
            preferences[NAME_KEY] = user.name!!
            preferences[PHONE_KEY] = user.phone!!
            preferences[PASSWORD_KEY] = user.password!!
            preferences[TOKEN_KEY] = user.token!!
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = ""
            preferences[NAME_KEY] = ""
            preferences[PHONE_KEY] = ""
            preferences[PASSWORD_KEY] = ""
            preferences[TOKEN_KEY] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NAME_KEY = stringPreferencesKey("name")
        private val PHONE_KEY = stringPreferencesKey("phone")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }

    }
}