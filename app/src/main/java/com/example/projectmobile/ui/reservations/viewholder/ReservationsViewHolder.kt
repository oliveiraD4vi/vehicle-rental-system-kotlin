package com.example.projectmobile.ui.reservations.viewholder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.R
import com.example.projectmobile.api.types.Cars
import com.example.projectmobile.api.types.Reservations
import com.example.projectmobile.databinding.RowReservationsBinding
import java.text.SimpleDateFormat
import java.util.*

class ReservationsViewHolder(
    private val bind: RowReservationsBinding,
    private val onItemClick: (Reservations) -> Unit
) : RecyclerView.ViewHolder(bind.root) {
    fun bind(reservations: Reservations) {
        val date = dateFormatter(reservations.pickup) + " - " + dateFormatter(reservations.devolution)
        bind.textDateWithdrawalDelivery.text = date
    }

    private fun dateFormatter(dataString: String): String {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return exitFormat.format(entryFormat.parse(dataString))
    }
}