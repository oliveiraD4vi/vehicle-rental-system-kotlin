package com.example.projectmobile.ui.reservations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.Reservation
import com.example.projectmobile.databinding.ActivityReservationsDetailsBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ReservationsDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityReservationsDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        getVehicleData()

        binding.imageBackDetails.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.image_back_details) {
            finish()
        }
    }

    private fun getVehicleData() {
        val preferencesManager = UserPreferencesManager(this)
        val apiService = APIService(preferencesManager.getToken())
        val reservation = preferencesManager.getSelectedReservation()
        val url = "/vehicle?id=${reservation?.vehicle_id}"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data = response.vehicle
                    if (data != null && reservation != null) {
                        runOnUiThread {
                            binding.textDaily.text = "DIÁRIA: R$ " + data.value.toString()
                            binding.textTotal.text = "TOTAL: R$ " + reservation.total_value.toString()
                            binding.textWithdrawalDetails.text =
                                "RETIRADA: " + dateFormatter(reservation.pickup)
                            binding.textDevolutionDetails.text =
                                "DEVOLUÇÃO: " + dateFormatter(reservation.devolution)
                            binding.textStatusDetails.text = "STATUS: " + reservation.status
                            binding.textStepDetails.text = "STEP: " + reservation.step
                            binding.textColorDetails.text = "COR: " + data.color
                            binding.textPlateDetails.text = "PLACA " + data.plate

                            binding.textNameCarDetails.text = data.brand + " " + data.model
                            binding.textPriceCar.text = "R$ " + data.value.toString()
                        }

                    }

                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@ReservationsDetailsActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@ReservationsDetailsActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun dateFormatter(dataString: String): String {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return exitFormat.format(entryFormat.parse(dataString))
    }
}
