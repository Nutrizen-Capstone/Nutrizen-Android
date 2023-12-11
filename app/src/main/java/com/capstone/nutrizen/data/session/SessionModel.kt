package com.capstone.nutrizen.data.session

data class SessionModel(
    val id :String,
    val name:String,
    val token: String,
    val email: String,
    val isLogin: Boolean = false,
    val isDataCompleted : Boolean = false,
    val birthDate : String,
    val age : Int,
    val gender : Int,// 1= male ,2=female
    val height : Double,
    val weight: Double,
    val activity : Int,
    val goal : Int
)