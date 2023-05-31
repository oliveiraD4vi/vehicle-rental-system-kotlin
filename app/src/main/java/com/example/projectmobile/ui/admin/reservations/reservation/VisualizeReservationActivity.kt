package com.example.projectmobile.ui.admin.reservations.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmobile.databinding.ActivityVisualizeReservationBinding

class VisualizeReservationActivity : AppCompatActivity() {
    private var _binding: ActivityVisualizeReservationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVisualizeReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.returnButton.setOnClickListener {
            finish()
        }
    }
}
