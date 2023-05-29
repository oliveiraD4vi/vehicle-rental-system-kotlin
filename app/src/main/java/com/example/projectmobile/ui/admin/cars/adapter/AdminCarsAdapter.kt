package com.example.projectmobile.ui.admin.cars.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.api.types.Car
import com.example.projectmobile.databinding.RowAdminItemBinding
import com.example.projectmobile.ui.admin.cars.viewholder.AdminCarsViewHolder

class AdminCarsAdapter : RecyclerView.Adapter<AdminCarsViewHolder>() {
    private var carsList: List<Car> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminCarsViewHolder {
        val item = RowAdminItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminCarsViewHolder(item)
    }

    override fun getItemCount(): Int {
        return carsList.count()
    }

    override fun onBindViewHolder(holder: AdminCarsViewHolder, position: Int) {
        holder.bind(carsList[position])
    }

    fun updateCars(list: List<Car>){
        carsList = list
        println(list)
        notifyDataSetChanged()
    }
}
