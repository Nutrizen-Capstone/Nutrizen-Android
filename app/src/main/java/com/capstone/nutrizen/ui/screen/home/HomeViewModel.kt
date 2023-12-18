package com.capstone.nutrizen.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutrizen.data.Repository
import com.capstone.nutrizen.data.retrofit.response.HistoryItem
import com.capstone.nutrizen.data.session.SessionModel
import com.capstone.nutrizen.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<HistoryItem>>> = MutableStateFlow(UiState.Loading)
    val uiStates: StateFlow<UiState<List<HistoryItem>>>
        get() = _uiState

    fun getHistory(token:String,id:String,date:String){
        viewModelScope.launch {
            try {
                val result = repository.getHistory(token,id, date)
                _uiState.value = UiState.Success(result.history)
            }
            catch (e: Exception){
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }
    fun getSession(): LiveData<SessionModel> {
        return repository.getSession().asLiveData()
    }

}