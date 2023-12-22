package com.capstone.nutrizen.ui.screen.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutrizen.data.Repository
import com.capstone.nutrizen.data.retrofit.response.DeleteHistoryResponse
import com.capstone.nutrizen.data.retrofit.response.GetHistoryResponse
import com.capstone.nutrizen.data.session.SessionModel
import com.capstone.nutrizen.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: Repository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<GetHistoryResponse>> =
        MutableStateFlow(UiState.Loading)
    val uiStates: StateFlow<UiState<GetHistoryResponse>>
        get() = _uiState

    private val _deleteResponse = MutableLiveData<DeleteHistoryResponse>()
    val deleteResponse: LiveData<DeleteHistoryResponse> = _deleteResponse

    fun deleteHistory(
        token: String, historyId: String
    ) {
        viewModelScope.launch {
            try {
                //get success message
                val response = repository.deleteHistory(token, historyId)
                _deleteResponse.postValue(response)

                Log.d(TAG, "onSuccess: ${response.message}")
            } catch (e: Exception) {
                //get error message
                Log.d(TAG, "onError: $e")
            }
        }
    }

    fun getHistory(token: String, id: String, date: String) {
        viewModelScope.launch {
            try {
                val result = repository.getHistory(token, id, date)
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }


    fun getSession(): LiveData<SessionModel> {
        return repository.getSession().asLiveData()
    }

    companion object {
        private const val TAG = "AddViewModel"
    }
}