package com.example.projectmobile.ui.cars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmobile.api.types.Cars

class CarsViewModel : ViewModel() {
    private var _carsList = MutableLiveData<ArrayList<Cars>>()
    var carsList: LiveData<ArrayList<Cars>> = _carsList
}
