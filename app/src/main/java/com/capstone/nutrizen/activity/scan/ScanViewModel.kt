package com.capstone.nutrizen.activity.scan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutrizen.data.Repository
import com.capstone.nutrizen.data.retrofit.response.AddHistoryResponse
import com.capstone.nutrizen.data.session.SessionModel
import kotlinx.coroutines.launch

class ScanViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        private const val TAG = "AddViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _addResponse = MutableLiveData<AddHistoryResponse>()
    val addResponse: LiveData<AddHistoryResponse> = _addResponse

    fun addHistory(
        token: String,id: String, nameFood: String, eatTime: String, calorie: Int, portion: Double, total: Int
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                //get success message
                val response = repository.addHistory(
                    token, id,nameFood, eatTime, calorie, portion, total
                )
                _addResponse.postValue(response)
                _isLoading.value = false

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

}