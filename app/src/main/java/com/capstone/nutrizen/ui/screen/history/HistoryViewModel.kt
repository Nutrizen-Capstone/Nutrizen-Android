package com.capstone.nutrizen.ui.screen.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.nutrizen.data.Repository
import com.capstone.nutrizen.data.retrofit.response.ListStoryItem
import com.capstone.nutrizen.data.session.SessionModel
import com.capstone.nutrizen.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: Repository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<ListStoryItem>>> = MutableStateFlow(UiState.Loading)
    val uiStates: StateFlow<UiState<List<ListStoryItem>>>
        get() = _uiState

    fun getHistory(token:String){
        viewModelScope.launch {
            try {
                val result = repository.getHistory(token)
                _uiState.value = UiState.Success(result.listStory)
            }
            catch (e: Exception){
                _uiState.value = UiState.Error(e.message.toString())
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
}