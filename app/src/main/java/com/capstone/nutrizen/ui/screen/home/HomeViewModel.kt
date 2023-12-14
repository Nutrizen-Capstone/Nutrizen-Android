package com.capstone.nutrizen.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.nutrizen.data.Repository
import com.capstone.nutrizen.data.session.SessionModel

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession().asLiveData()
    }

}