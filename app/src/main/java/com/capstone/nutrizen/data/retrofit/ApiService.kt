package com.capstone.nutrizen.data.retrofit

import com.capstone.nutrizen.activity.dataform.DataForm
import com.capstone.nutrizen.activity.register.DataRegister
import com.capstone.nutrizen.data.retrofit.response.GetStoryResponse
import com.capstone.nutrizen.data.retrofit.response.LoginResponse
import com.capstone.nutrizen.data.retrofit.response.PersonalDataResponse
import com.capstone.nutrizen.data.retrofit.response.SignupResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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
    @POST("register")
    suspend fun register2(
       @Body dataRegister: DataRegister
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
    @PUT("profile/{id}")
    suspend fun personalData2(
        @Path("id") id: String,
        @Body dataForm: DataForm
    ): PersonalDataResponse

   /* @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignupResponse*/

    @GET("stories")
    suspend fun getStories(
    ): GetStoryResponse

}