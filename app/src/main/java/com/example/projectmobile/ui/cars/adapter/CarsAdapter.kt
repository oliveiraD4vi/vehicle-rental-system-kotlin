package com.example.projectmobile.ui.cars.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.databinding.RowCarBinding
import com.example.projectmobile.api.types.Cars
import com.example.projectmobile.ui.cars.viewholder.CarsViewHolder

class CarsAdapter: RecyclerView.Adapter<CarsViewHolder>() {
    private var carsList = ArrayList<Cars>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
        val item = RowCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarsViewHolder(item)
    }

    override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
        holder.bind(carsList[position])
    }

    override fun getItemCount(): Int {
        return carsList.count()
    }

    fun updatedCars(list: ArrayList<Cars>){
        carsList = list
        notifyDataSetChanged()
    }

}