package com.capstone.nutrizen.activity.dataresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.nutrizen.data.Repository
import com.capstone.nutrizen.data.session.SessionModel

class MyDataViewModel(private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession().asLiveData()
    }

}