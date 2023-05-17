package com.example.projectmobile.api.types

data class AuthData(
    val userId: Long,
    val token: String,
    val role: String,
)
