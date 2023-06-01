package com.example.projectmobile.ui.admin.cars.car

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmobile.databinding.ActivityCreateCarBinding

class CreateCarActivity : AppCompatActivity() {
    private var _binding: ActivityCreateCarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.returnButton.setOnClickListener {
            finish()
        }
    }
}
