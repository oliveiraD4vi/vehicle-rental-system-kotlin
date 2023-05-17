package com.example.projectmobile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _dataWithdrawal = MutableLiveData<String>().apply {
        value = "ESCOLHA UMA DATA"
    }

    private val _dataDelivery = MutableLiveData<String>().apply {
        value = "ESCOLHA UMA DATA"
    }

    val dataDelivery: LiveData<String> = _dataDelivery
    val dataWithdrawal: LiveData<String> = _dataWithdrawal
}