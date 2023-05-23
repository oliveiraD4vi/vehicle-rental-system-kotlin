package com.example.projectmobile.ui.cars.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.R
import com.example.projectmobile.databinding.RowCarBinding
import com.example.projectmobile.api.types.Cars

class CarsViewHolder(private val bind: RowCarBinding) : RecyclerView.ViewHolder(bind.root) {
    fun bind(car: Cars){
        val nameCar = itemView.context.getString(R.string.car_name, car.brand, car.model)
        val priceCar = itemView.context.getString(R.string.car_price, "R$", car.value.toString())

        bind.textNameCarRow.text = nameCar
        bind.textPriceCar.text = priceCar
    }
}
