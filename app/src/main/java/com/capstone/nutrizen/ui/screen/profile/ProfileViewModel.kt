package com.capstone.nutrizen.ui.screen.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutrizen.data.Repository
import com.capstone.nutrizen.data.retrofit.response.DeleteUserResponse
import com.capstone.nutrizen.data.session.SessionModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    private val _deleteResponse = MutableLiveData<DeleteUserResponse>()
    val deleteResponse: LiveData<DeleteUserResponse> = _deleteResponse

    fun deleteUser(
        token: String, userId: String
    ) {
        viewModelScope.launch {
            try {
                //get success message
                val response = repository.deleteUser(token, userId)
                _deleteResponse.postValue(response)

                Log.d(TAG, "onSuccess: ${response.message}")
            } catch (e: Exception) {
                //get error message
                Log.d(TAG, "onError: $e")
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    fun getSession(): LiveData<SessionModel> {
        return repository.getSession().asLiveData()
    }
    companion object {
        private const val TAG = "ProfileViewModel"
    }
}