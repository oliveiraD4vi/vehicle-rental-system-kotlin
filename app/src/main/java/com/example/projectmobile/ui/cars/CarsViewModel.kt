package com.example.projectmobile.ui.cars

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmobile.MainActivity
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.model.Cars
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.UserData
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException
import java.util.Objects

class CarsViewModel : ViewModel() {

    private val _carsList = MutableLiveData<ArrayList<Cars>>()
    val carsList: LiveData<ArrayList<Cars>> = _carsList



}