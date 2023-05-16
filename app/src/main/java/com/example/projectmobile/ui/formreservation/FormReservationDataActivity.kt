package com.example.projectmobile.ui.formreservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projectmobile.MainActivity
import com.example.projectmobile.R
import com.example.projectmobile.databinding.ActivityFormReservationDataBinding

class FormReservationDataActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFormReservationDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormReservationDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.imageBackDataForm.setOnClickListener(this)
        binding.buttonCancelDataForm.setOnClickListener(this)
        binding.buttonNextDataForm.setOnClickListener(this)
    }

    override fun onClick(view: View){
        if(view.id == R.id.image_back_data_form){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if(view.id == R.id.button_cancel_data_form){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if(view.id == R.id.button_next_data_form) {
            startActivity(Intent(this, FormReservationVehicleActivity::class.java))
        }
    }
}