package com.example.projectmobile.ui.admin.reservations

import com.example.projectmobile.api.types.Reservation

interface ReservationClickListener {
    fun onEditCarClick(reservation: Reservation)
    fun onDeleteCarClick(reservation: Reservation)
}
