package com.example.projectmobile.ui.reservations.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.api.types.Cars
import com.example.projectmobile.databinding.RowReservationsBinding

class ReservationsViewHolder(
    private val bind: RowReservationsBinding,
    private val onItemClick: (Cars) -> Unit
) : RecyclerView.ViewHolder(bind.root) {
    fun bind(reservation: Cars) {

    }
}