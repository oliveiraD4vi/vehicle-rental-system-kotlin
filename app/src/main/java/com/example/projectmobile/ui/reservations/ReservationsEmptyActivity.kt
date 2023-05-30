package com.example.projectmobile.ui.reservations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projectmobile.LoginActivity
import com.example.projectmobile.MainActivity
import com.example.projectmobile.R
import com.example.projectmobile.databinding.ActivityMainBinding
import com.example.projectmobile.databinding.ActivityReservationsEmptyBinding

class ReservationsEmptyActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityReservationsEmptyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationsEmptyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.imageBackEmpty.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if(view.id == R.id.image_back_empty){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}