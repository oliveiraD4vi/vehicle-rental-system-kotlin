package com.example.projectmobile.ui.admin.cars.car

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmobile.R
import com.example.projectmobile.databinding.ActivityVisualizeCarBinding
import com.example.projectmobile.databinding.ActivityVisualizeUserBinding

class VisualizeCarActivity : AppCompatActivity() {
    private var _binding: ActivityVisualizeUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVisualizeUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.returnButton.setOnClickListener {
            finish()
        }
    }
}
