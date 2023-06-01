package com.example.projectmobile.ui.admin.users.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmobile.databinding.ActivityCreateUserBinding

class CreateUserActivity : AppCompatActivity() {
    private var _binding: ActivityCreateUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.returnButton.setOnClickListener {
            finish()
        }
    }
}
