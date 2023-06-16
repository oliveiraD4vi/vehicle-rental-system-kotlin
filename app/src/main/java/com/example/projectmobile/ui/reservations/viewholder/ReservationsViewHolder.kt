package com.example.projectmobile.ui.reservations.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.api.types.Reservation
import com.example.projectmobile.api.types.Status
import com.example.projectmobile.databinding.RowReservationsBinding
import java.text.SimpleDateFormat
import java.util.*

class ReservationsViewHolder(
    private val bind: RowReservationsBinding,
    private val onItemClick: (Reservation) -> Unit
) : RecyclerView.ViewHolder(bind.root) {
    fun bind(reservation: Reservation) {
        val date = dateFormatter(reservation.pickup) + " - " + dateFormatter(reservation.devolution)
        bind.textDateWithdrawalDelivery.text = date
        if (reservation.status == Status.CREATED || reservation.status == Status.PICKUP || reservation.status == Status.CONFIRMED) {
            bind.imageAttention.visibility = View.VISIBLE
        } else if (reservation.status == Status.FINALIZED) {
            bind.imageCheck.visibility = View.VISIBLE
        }

        bind.root.setOnClickListener {
            onItemClick(reservation)
        }
    }

    private fun dateFormatter(dataString: String): String? {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return entryFormat.parse(dataString)?.let { exitFormat.format(it) }
    }
}
