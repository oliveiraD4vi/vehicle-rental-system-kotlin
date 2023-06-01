package com.example.projectmobile.ui.admin.reservations.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmobile.databinding.ActivityCreateReservationBinding

class CreateReservationActivity : AppCompatActivity() {
    private var _binding: ActivityCreateReservationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.returnButton.setOnClickListener {
            finish()
        }
    }
}
