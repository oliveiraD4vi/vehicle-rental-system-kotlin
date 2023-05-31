package com.example.projectmobile.api.types

data class Reservation(
    val id: Int,
    val userId: Int,
    val vehicleId: Int,
    val pickup: String,
    val devolution: String,
    val step: Step,
    val status: Status,
    val totalValue: Float,
)
