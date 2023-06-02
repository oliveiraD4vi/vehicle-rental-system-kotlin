package com.example.projectmobile.ui.admin.cars.clicklistener

import com.example.projectmobile.api.types.Car

interface CarClickListener {
    fun onEditCarClick(car: Car)
    fun onDeleteCarClick(car: Car)
}
