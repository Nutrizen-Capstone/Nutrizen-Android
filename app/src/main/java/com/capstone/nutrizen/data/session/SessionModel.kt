package com.capstone.nutrizen.data.session

data class SessionModel(
    val id :String,
    val name:String,
    val token: String,
    val email: String,
    val isLogin: Boolean = false
)