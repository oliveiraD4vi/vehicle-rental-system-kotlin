package com.example.projectmobile.ui.cars.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.databinding.RowCarBinding
import com.example.projectmobile.api.types.Car
import com.example.projectmobile.ui.cars.viewholder.CarsViewHolder

class CarsAdapter(private val onItemClick: (Car) -> Unit) : RecyclerView.Adapter<CarsViewHolder>() {
    private var carsList: List<Car> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
        val item = RowCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarsViewHolder(item, onItemClick)
    }

    override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
        holder.bind(carsList[position])
    }

    override fun getItemCount(): Int {
        return carsList.count()
    }

    fun updatedCars(list: List<Car>){
        carsList = list
        notifyDataSetChanged()
    }
}
