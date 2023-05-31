package com.example.projectmobile.ui.reservations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmobile.api.types.Reservations
import com.example.projectmobile.databinding.ActivityReservationsDetailsBinding
import com.example.projectmobile.util.UserPreferencesManager

class ReservationsDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationsDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val preferencesManager = UserPreferencesManager(this)

        val reservations: Reservations? = preferencesManager.getSelectedReservation()
        println(reservations)
    }
}