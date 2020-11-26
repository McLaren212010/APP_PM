package com.example.app_pm.api

import retrofit2.Call
import retrofit2.http.*


interface EndPoints {
    @GET("/users/")
    fun getUsers(): Call<List<User>>

    @GET("/users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @FormUrlEncoded
    @POST("/posts")
    fun postTest(@Field("title")first: String?): Call<OutputPost>
}