package com.capstone.nutrizen.activity.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.nutrizen.data.Repository

class AddViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        private const val TAG = "AddViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


}