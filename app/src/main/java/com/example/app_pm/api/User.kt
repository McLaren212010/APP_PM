package com.example.app_pm.api

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val address: Address
)

data class Address(
    val city: String,
    val zipcode: String,
    val geo: Geo
)

data class Geo(
    val lat: Int,
    val lng: Int
)