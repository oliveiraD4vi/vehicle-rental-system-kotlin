package com.example.projectmobile.ui.cars.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.databinding.RowCarBinding
import com.example.projectmobile.model.Cars

class CarsViewHolder(private val bind: RowCarBinding) : RecyclerView.ViewHolder(bind.root) {
    fun bind(car: Cars){
        bind.textNameCarRow.text = car.brand + " " + car.model
        bind.textPriceCar.text = "R$ " + car.value.toString()
    }
}