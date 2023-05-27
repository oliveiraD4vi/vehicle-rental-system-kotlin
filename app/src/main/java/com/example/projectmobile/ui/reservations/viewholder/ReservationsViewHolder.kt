package com.example.projectmobile.ui.reservations.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.R
import com.example.projectmobile.api.types.Reservation
import com.example.projectmobile.databinding.RowReservationsBinding

class ReservationsViewHolder(
    private val bind: RowReservationsBinding,
    private val onItemClick: (Reservation) -> Unit
) : RecyclerView.ViewHolder(bind.root) {
    fun bind(reservation: Reservation) {

    }
}