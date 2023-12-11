package com.capstone.nutrizen.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class SessionPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: SessionModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.id
            preferences[NAME_KEY] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[EMAIL_KEY] = user.email
            preferences[IS_LOGIN_KEY] = user.isLogin
            preferences[IS_DataCompleted_KEY] = user.isDataCompleted
            preferences[BirthDate_KEY] = user.birthDate
            preferences[Age_KEY] = user.age
            preferences[Gender_KEY] = user.gender
            preferences[Height_KEY] = user.height
            preferences[Weight_KEY] = user.weight
            preferences[Activity_KEY] = user.activity
            preferences[Goal_KEY] = user.goal
        }
    }

    fun getSession(): Flow<SessionModel> {
        return dataStore.data.map { preferences ->
            SessionModel(
                preferences[ID_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[IS_DataCompleted_KEY] ?: false,
                preferences[BirthDate_KEY] ?: "",
                preferences[Age_KEY] ?: 0,
                preferences[Gender_KEY] ?: 0,
                preferences[Height_KEY] ?: 0.0,
                preferences[Weight_KEY] ?: 0.0,
                preferences[Activity_KEY] ?: 0,
                preferences[Goal_KEY] ?: 0,
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreference? = null

        private val ID_KEY = stringPreferencesKey("id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val IS_DataCompleted_KEY = booleanPreferencesKey("isDataCompleted")
        private val BirthDate_KEY = stringPreferencesKey("birthDate")
        private val Age_KEY = intPreferencesKey("age")
        private val Gender_KEY = intPreferencesKey("gender")
        private val Height_KEY = doublePreferencesKey("height")
        private val Weight_KEY = doublePreferencesKey("weight")
        private val Activity_KEY = intPreferencesKey("activity")
        private val Goal_KEY = intPreferencesKey("goal")


        fun getInstance(dataStore: DataStore<Preferences>): SessionPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}