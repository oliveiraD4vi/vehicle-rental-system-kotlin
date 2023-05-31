package com.example.projectmobile.ui.admin.users.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmobile.databinding.ActivityVisualizeCarBinding

class VisualizeUserActivity : AppCompatActivity() {
    private var _binding: ActivityVisualizeCarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVisualizeCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.returnButton.setOnClickListener {
            finish()
        }
    }
}
