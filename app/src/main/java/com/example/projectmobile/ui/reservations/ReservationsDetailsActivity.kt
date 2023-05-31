package com.example.projectmobile.ui.reservations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projectmobile.R
import com.example.projectmobile.api.types.Reservations
import com.example.projectmobile.databinding.ActivityReservationsDetailsBinding
import com.example.projectmobile.util.UserPreferencesManager

class ReservationsDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityReservationsDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val preferencesManager = UserPreferencesManager(this)

        val reservations: Reservations? = preferencesManager.getSelectedReservation()
        binding.imageBackDetails.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if(view.id == R.id.image_back_details){
            finish()
        }
    }
}