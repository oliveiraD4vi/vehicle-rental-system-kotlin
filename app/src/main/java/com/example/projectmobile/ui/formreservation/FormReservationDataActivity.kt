package com.example.projectmobile.ui.formreservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    override fun onClick(view: View) {
        if (view.id == R.id.image_back_data_form) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if (view.id == R.id.button_cancel_data_form) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if (view.id == R.id.button_next_data_form) {
            if (validateData()) {
                startActivity(Intent(this, FormReservationVehicleActivity::class.java))
            } else {
                Toast.makeText(
                    applicationContext,
                    "Os campos devem ser preenchidos corretamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validateData(): Boolean {
        if (binding.editPhone.text.toString().length < 10) {
            binding.editPhone.error = "Número Inválido"
            return false
        } else if (binding.editRoad.text.toString() == "") {
            binding.editRoad.error = "Campo Obrigatório"
            return false
        } else if (binding.editNumber.text.toString() == "") {
            binding.editNumber.error = "Campo Obrigatório"
            return false
        } else if (binding.editNeighborhood.text.toString() == "") {
            binding.editNeighborhood.error = "Campo Obrigatório"
            return false
        } else if (binding.editCity.text.toString() == "") {
            binding.editCity.error = "Campo Obrigatório"
            return false
        } else if (binding.editState.text.toString() == "") {
            binding.editState.error = "Campo Obrigatório"
            return false
        } else if (binding.editCountry.text.toString() == "") {
            binding.editCountry.error = "Campo Obrigatório"
            return false
        }
        return true
    }
}