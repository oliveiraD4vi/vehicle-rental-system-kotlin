package com.example.projectmobile.ui.admin.reservations.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.api.types.Reservation
import com.example.projectmobile.databinding.RowAdminItemBinding
import com.example.projectmobile.ui.admin.reservations.clicklistener.ReservationClickListener
import java.text.SimpleDateFormat
import java.util.*

class AdminReservationViewHolder(private val bind: RowAdminItemBinding, private val carClickListener: ReservationClickListener): RecyclerView.ViewHolder(bind.root) {
    fun bind(reservation: Reservation) {
        val reservationString = "${dateFormatter(reservation.pickup)} - ${dateFormatter(reservation.devolution)}"

        bind.itemContent.text = reservationString

        bind.arrowRight.setOnClickListener {
            carClickListener.onEditCarClick(reservation)
        }

        bind.trash.setOnClickListener {
            carClickListener.onDeleteCarClick(reservation)
        }
    }

    private fun dateFormatter(dataString: String?): String {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        if (dataString != null) {
            return exitFormat.format(entryFormat.parse(dataString)!!)
        }

        return "?/?/?"
    }
}
