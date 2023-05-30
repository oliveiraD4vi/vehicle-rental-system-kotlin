package com.example.projectmobile.ui.admin.reservations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.api.types.Reservation
import com.example.projectmobile.databinding.RowAdminItemBinding
import com.example.projectmobile.ui.admin.reservations.viewholder.AdminReservationViewHolder

class AdminReservationAdapter : RecyclerView.Adapter<AdminReservationViewHolder>() {
    private var reservationList: List<Reservation> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminReservationViewHolder {
        val item = RowAdminItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminReservationViewHolder(item)
    }

    override fun getItemCount(): Int {
        return reservationList.count()
    }

    override fun onBindViewHolder(holder: AdminReservationViewHolder, position: Int) {
        holder.bind(reservationList[position])
    }

    fun updateReservations(list: List<Reservation>) {
        reservationList = list
        notifyDataSetChanged()
    }
}
