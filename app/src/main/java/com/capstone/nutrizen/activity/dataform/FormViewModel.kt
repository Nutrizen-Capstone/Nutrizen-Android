package com.capstone.nutrizen.activity.dataform

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutrizen.data.Repository
import com.capstone.nutrizen.data.retrofit.response.LoginResponse
import com.capstone.nutrizen.data.session.SessionModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class FormViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        private const val TAG = "DataViewModel"
    }

    // sementara sebelum dapat endpoint numpang endoint dicoding
    private val _formResponse = MutableLiveData<LoginResponse>()
    val FormResponse: LiveData<LoginResponse> = _formResponse

    fun save(
        email: String,
        password: String,
        birthDate: String,
        age: Int,
        gender: Int,
        height: Double,
        weight: Double,
        activity: Int,
        goal: Int
    ) {
        viewModelScope.launch {
            try {
                //get success message
                val response = repository.login(email, password)
                _formResponse.postValue(response)
                saveSession(
                    SessionModel(
                        response.loginResult.userId,
                        response.loginResult.name,
                        response.loginResult.token,
                        email,
                        true,
                        true,
                        birthDate,
                        age,
                        gender,
                        height,
                        weight,
                        activity,
                        goal
                    )
                )
                Log.d(TAG, "onSuccess: ${response.message}")
            } catch (e: HttpException) {
                //get error message
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                _formResponse.postValue(errorBody)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession().asLiveData()
    }

    private fun saveSession(user: SessionModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}