package com.capstone.nutrizen.activity.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrizen.data.Repository
import com.capstone.nutrizen.data.retrofit.response.SignupResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class RegisterViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        private const val TAG = "SignupViewModel"
    }

    private val _signupResponse = MutableLiveData<SignupResponse>()
    val signupResponse: LiveData<SignupResponse> = _signupResponse

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun register(name: String, email: String, password: String) {
        _loading.value = true
        viewModelScope.launch {
            viewModelScope.launch {
                try {
                    //get success message
                    val response = repository.signup(name, email, password)
                    _signupResponse.postValue(response)
                    _loading.value = false
                    Log.d(TAG, "onSuccess: ${response.message}")
                } catch (e: HttpException) {
                    //get error message
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, SignupResponse::class.java)
                    val errorMessage = errorBody.message
                    _signupResponse.postValue(errorBody)
                    _loading.value= false
                    Log.d(TAG, "onError: $errorMessage")
                }
            }
        }
    }
}