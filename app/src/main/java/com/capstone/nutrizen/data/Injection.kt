package com.capstone.nutrizen.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.nutrizen.data.retrofit.ApiConfig
import com.capstone.nutrizen.data.session.SessionPreference


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = SessionPreference.getInstance(context.dataStore)
        val api = ApiConfig.getApiService()
        return Repository.getInstance(pref,api)
    }
}