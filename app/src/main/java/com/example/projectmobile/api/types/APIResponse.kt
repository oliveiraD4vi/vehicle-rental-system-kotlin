package com.example.projectmobile.api.types

data class APIResponse(
    val error: Boolean,
    val data: Any?,
    val vehicles: List<Cars>?,
    val reservations: List<Any>?,
    val message: String,
    val authData: AuthData?,
    val user: UserData?,
    val users: List<UserData>?,
)
