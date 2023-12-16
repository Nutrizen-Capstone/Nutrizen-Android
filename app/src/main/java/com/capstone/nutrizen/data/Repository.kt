package com.capstone.nutrizen.data

import com.capstone.nutrizen.data.retrofit.ApiConfig
import com.capstone.nutrizen.data.retrofit.ApiService
import com.capstone.nutrizen.data.session.SessionModel
import com.capstone.nutrizen.data.session.SessionPreference
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val sessionPreference: SessionPreference,
    private val api: ApiService
) {
    //repository for api request
    suspend fun signup(name: String, email: String, password: String) =
        api.register(name, email, password)

    suspend fun login(email: String, password: String) =
        api.login(email, password)

    suspend fun getHistory(token:String) =
        ApiConfig.getApiToken(token).getStories()


    //repository for preference of session
    suspend fun saveSession(user: SessionModel) {
        sessionPreference.saveSession(user)
    }

    fun getSession(): Flow<SessionModel> {
        return sessionPreference.getSession()
    }

    suspend fun logout() {
        sessionPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            sessionPreference: SessionPreference,
            api: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(sessionPreference, api)
            }.also { instance = it }
    }
}