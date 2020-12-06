package com.example.app_pm.api

import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface EndPoints {
    @GET("/user/")
    fun getUsers(): Call<List<User>>

    @GET("/user/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @FormUrlEncoded
    @POST("/myslim/api/user")
    fun login(@Field("name") name: String, @Field("password") password: String?): Call<OutputPost>
}