package com.capstone.nutrizen.data.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddHistoryResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("scanResult")
	val scanResult: ScanResult
) : Parcelable

@Parcelize
data class ScanResult(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("eatTime")
	val eatTime: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("historyId")
	val historyId: String,

	@field:SerializedName("nameFood")
	val nameFood: String,

	@field:SerializedName("portion")
	val portion: Double,

	@field:SerializedName("calorie")
	val calorie: Int,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
) : Parcelable
