package com.capstone.nutrizen.activity.login

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun login(email: String, password: String) {
        _isLoading.value=true

        viewModelScope.launch {
            try {
                //get success message
                val response = repository.login(email, password)
                _loginResponse.postValue(response)
                _isLoading.value= false

                saveSession(
                    // semuanya berdasarkan respose.data kecuali islogin =true
                    SessionModel(
                        response.loginResult.userId,
                        response.loginResult.name,
                        response.loginResult.token,
                        response.loginResult.email,
                        true,
                        response.loginResult.isDataCompleted,
                        response.loginResult.birthDate,
                        response.loginResult.age,
                        response.loginResult.gender,
                        response.loginResult.height,
                        response.loginResult.weight,
                        response.loginResult.activity,
                        response.loginResult.goal,

                        )
                )
                Log.d(TAG, "onSuccess: ${response.message}")
            } catch (e: HttpException) {
                //get error message
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                _loginResponse.postValue(errorBody)
                _isLoading.value= false
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

    private val mutableStateFlow = MutableStateFlow(true)
    val splash = mutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            mutableStateFlow.value = false
        }
    }
}