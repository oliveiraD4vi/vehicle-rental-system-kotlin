package com.example.projectmobile.api.types

data class Reservation(
    val id: Int,
    val user_id: Int,
    val vehicle_id: Int,
    val pickup: String,
    val devolution: String,
    val step: Step,
    val status: Status,
    val total_value: Float,
)
