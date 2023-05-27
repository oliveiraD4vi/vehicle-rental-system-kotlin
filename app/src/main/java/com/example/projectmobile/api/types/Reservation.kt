package com.example.projectmobile.api.types

data class Reservation(
    val user_id: Int,
    val vehicle_id: Int,
    val pickup: String,
    val devolution: String,
    val status: String,
    val step: String
)