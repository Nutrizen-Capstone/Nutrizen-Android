package com.capstone.nutrizen.data.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class LoginResult(

	@field:SerializedName("goal")
	val goal: Int,

	@field:SerializedName("gender")
	val gender: Int,

	@field:SerializedName("activity")
	val activity: Int,

	@field:SerializedName("weight")
	val weight: Double,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("birthDate")
	val birthDate: String,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("isDataCompleted")
	val isDataCompleted: Boolean,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("height")
	val height: Double
) : Parcelable
