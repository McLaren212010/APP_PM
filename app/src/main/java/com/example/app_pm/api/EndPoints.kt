package com.example.app_pm.api

import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface EndPoints {
    @GET("/myslim/api/problem")
    fun getProblem(): Call<List<Problem>>

    @FormUrlEncoded
    @POST("/myslim/api/user")
    fun login(@Field("name") name: String, @Field("password") password: String?): Call<OutputPost>

    @FormUrlEncoded
    @POST("/myslim/api/insertmarker")
    fun insert(@Field("descr")descr: String, @Field("lat")lat:String, @Field("lng")lng:String,@Field("user_id")user_id:Int): Call<OutputPost2>

    @GET("/myslim/api/filter/{user_id}")
    fun getProblemByID(@Path("user_id")user_id:Int):Call<List<Problem>>
}