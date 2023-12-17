package com.capstone.nutrizen.data

import com.capstone.nutrizen.activity.dataform.DataForm
import com.capstone.nutrizen.activity.register.DataRegister
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

    suspend fun register2(dataRegister: DataRegister) =
        api.register2(dataRegister)

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
        ApiConfig.getApiToken(token).personalData(id, photoUrl, birthDate, age, gender, weight, height, activity, goal)

    suspend fun personalData2(token: String,id: String,dataForm: DataForm) =
        ApiConfig.getApiToken(token).personalData2(id, dataForm)

    /*suspend fun signup(name: String, email: String, password: String) =
        api.register(name, email, password)*/

    suspend fun login(email: String, password: String) =
        api.login(email, password)

    suspend fun getHistory(token: String) =
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