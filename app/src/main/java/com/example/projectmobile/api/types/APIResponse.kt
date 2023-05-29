package com.example.projectmobile.api.types

data class APIResponse(
    val error: Boolean,
    val data: Any?,
    val vehicles: List<Cars>?,
    val Car: Cars,
    val reservations: List<Reservations>?,
    val message: String,
    val authData: AuthData?,
    val user: UserData?
)
