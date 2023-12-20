package com.capstone.nutrizen.activity.dataform

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutrizen.data.Repository
import com.capstone.nutrizen.data.retrofit.response.PersonalDataResponse
import com.capstone.nutrizen.data.session.SessionModel
import kotlinx.coroutines.launch


class FormViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        private const val TAG = "DataViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // sementara sebelum dapat endpoint numpang endoint dicoding
    private val _formResponse = MutableLiveData<PersonalDataResponse>()
    val FormResponse: LiveData<PersonalDataResponse> = _formResponse

    fun save(
        token: String,
        id: String,
        photoUrl: String,
        birthDate: String,
        age: Int,
        gender: Int,
        height: Double,
        weight: Double,
        activity: Int,
        goal: Int,
        email: String,
        name: String,
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                //get success message
                val response = repository.personalData(
                    token,
                    id,
                    photoUrl,
                    birthDate,
                    age,
                    gender,
                    height,
                    weight,
                    activity,
                    goal
                )
                _formResponse.postValue(response)
                _isLoading.value = false

                saveSession(
                    SessionModel(
                        id,
                        name,
                        token,
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
            } catch (e: Exception) {
                //get error message
                _isLoading.value = false
                Log.d(TAG, "onError: $e")
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
