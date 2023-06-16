package com.example.projectmobile.ui.formreservation.data

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.projectmobile.MainActivity
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.ActivityFormReservationDataBinding
import com.example.projectmobile.ui.formreservation.vehicle.FormReservationVehicleActivity
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class FormReservationDataActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFormReservationDataBinding
    private lateinit var preferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormReservationDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        preferencesManager = UserPreferencesManager(this)

        binding.returnButton.setOnClickListener(this)
        binding.buttonCancelDataForm.setOnClickListener(this)
        binding.buttonNextDataForm.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.returnButton) {
            finish()
        } else if (view.id == R.id.button_cancel_data_form) {
            deleteReservation()
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

    private fun deleteReservation() {
//        loading()
        val apiService = APIService(preferencesManager.getToken())
        val reservationId = preferencesManager.getReservationId()
        val url = "/reservation?id=$reservationId"

        apiService.deleteData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val intent = Intent(this@FormReservationDataActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    val errorCode = response.message

                    runOnUiThread {
//                    loaded()
                        Toast.makeText(
                            this@FormReservationDataActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
//                    loaded()
                    Toast.makeText(
                        this@FormReservationDataActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
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
