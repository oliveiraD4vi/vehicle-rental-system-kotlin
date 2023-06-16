package com.example.projectmobile.api.types

data class APIResponse(
    val error: Boolean,
    val data: Any?,
    val vehicles: List<Car>?,
    val reservations: List<Reservation>?,
    val reservation: Reservation,
    val vehicle: Car?,
    val message: String,
    val authData: AuthData?,
    val user: User?,
    val users: List<User>?,
)
