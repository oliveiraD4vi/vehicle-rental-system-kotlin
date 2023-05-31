package com.example.projectmobile.ui.admin.cars

import com.example.projectmobile.api.types.Car

interface CarClickListener {
    fun onEditCarClick(car: Car)
    fun onDeleteCarClick(car: Car)
}
