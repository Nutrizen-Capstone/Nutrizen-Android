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
    suspend fun register(name: String, email: String, password: String, confPassword: String) =
        api.register(name, email, password, confPassword)

    suspend fun login(email: String, password: String) =
        api.login(email, password)

    suspend fun personalData(
        token: String,
        id: String,
        photoUrl: String,
        birthDate: String,
        age: Int,
        gender: Int,
        height: Double,
        weight: Double,
        activity: Int,
        goal: Int
    ) =
        ApiConfig.getApiToken(token)
            .personalData(id, photoUrl, birthDate, age, gender, weight, height, activity, goal)

    suspend fun addHistory(
        token: String,
        id: String,
        nameFood: String,
        eatTime: String,
        calorie: Int,
        portion: Double,
        total: Int
    ) =
        ApiConfig.getApiToken(token).addHistory(id, nameFood, eatTime, calorie, portion, total)

    suspend fun getHistory(token: String, id: String, date: String) =
        ApiConfig.getApiToken(token).getHistory(id, date)


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