package com.capstone.nutrizen.data.retrofit

import com.capstone.nutrizen.data.retrofit.response.AddHistoryResponse
import com.capstone.nutrizen.data.retrofit.response.GetHistoryResponse
import com.capstone.nutrizen.data.retrofit.response.LoginResponse
import com.capstone.nutrizen.data.retrofit.response.PersonalDataResponse
import com.capstone.nutrizen.data.retrofit.response.SignupResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confPassword") confPassword: String,
    ): SignupResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @PUT("profile/{id}")
    suspend fun personalData(
        @Path("id") id: String,
        @Field("photoUrl") photoUrl: String,
        @Field("birthDate") birthDate: String,
        @Field("age") age: Int,
        @Field("gender") gender: Int,
        @Field("weight") weight: Double,
        @Field("height") height: Double,
        @Field("activity") activity: Int,
        @Field("goal") goal: Int
    ): PersonalDataResponse


    @FormUrlEncoded
    @POST("scan")
    suspend fun addHistory(
        @Field("userId") id: String,
        @Field("nameFood") nameFood: String,
        @Field("eatTime") eatTime:String,
        @Field("calorie") calorie: Int,
        @Field("portion") portion: Double,
        @Field("total") total: Int,
    ): AddHistoryResponse
    //add field date later

    @FormUrlEncoded
    @POST("scanHistory")
    suspend fun getHistory(
        @Field("userId") id: String,
        @Field("date") date: String,
    ): GetHistoryResponse

    @FormUrlEncoded
    @POST("scanHistory")
    suspend fun deleteHistory(
        @Field("userId") id: String,
        @Field("date") date: String,
    ): GetHistoryResponse
    

}