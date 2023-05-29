package com.example.projectmobile.ui.admin.cars.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.R
import com.example.projectmobile.api.types.Car
import com.example.projectmobile.databinding.RowAdminItemBinding

class AdminCarsViewHolder(private val bind: RowAdminItemBinding): RecyclerView.ViewHolder(bind.root) {
    fun bind(car: Car) {
        val nameCar = itemView.context.getString(R.string.car_name, car.brand, car.model)

        bind.itemContent.text = nameCar
    }
}
