package com.example.app_pm.api


data class OutputPost(
    val error: Boolean,
    val data: Data,
    val mensagem: String
)

data class Data(
    val id: Int,
    val name: String,
    val password: String,
)


data class OutputPost2(
    val status: Boolean,
    val msg: String
)
