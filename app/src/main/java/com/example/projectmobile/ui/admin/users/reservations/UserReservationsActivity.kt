package com.example.projectmobile.ui.admin.users.reservations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmobile.databinding.ActivityMainBinding
import com.example.projectmobile.databinding.ActivityUserReservationsBinding

class UserReservationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserReservationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserReservationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }
}