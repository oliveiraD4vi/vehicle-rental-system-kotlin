package com.example.projectmobile.ui.reservations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.api.types.Reservation
import com.example.projectmobile.databinding.RowReservationsBinding
import com.example.projectmobile.ui.reservations.viewholder.ReservationsViewHolder

class ReservationsAdapter(private val onItemClick: (Reservation) -> Unit) :
    RecyclerView.Adapter<ReservationsViewHolder>() {
    private var reservationList: List<Reservation> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationsViewHolder {
        val item =
            RowReservationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationsViewHolder(item, onItemClick)
    }

    override fun onBindViewHolder(holder: ReservationsViewHolder, position: Int) {
        holder.bind(reservationList[position])
    }

    override fun getItemCount(): Int {
        return reservationList.count()
    }

    fun updatedReservations(list: List<Reservation>) {
        reservationList = list
        notifyDataSetChanged()
    }
}