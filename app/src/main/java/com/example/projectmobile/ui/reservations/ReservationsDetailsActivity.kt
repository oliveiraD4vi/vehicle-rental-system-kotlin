package com.example.projectmobile.ui.reservations

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.Car
import com.example.projectmobile.api.types.Reservation
import com.example.projectmobile.api.types.Status
import com.example.projectmobile.databinding.ActivityReservationsDetailsBinding
import com.example.projectmobile.databinding.ModalLayoutBinding
import com.example.projectmobile.ui.cars.CarsFragment
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
        binding.buttonDetails.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.image_back_details) {
            finish()
        } else if (view.id == R.id.button_details) {
            val preferencesManager = UserPreferencesManager(this)
            preferencesManager.getSelectedReservation()?.let { onDeleteCarClick(it) }
        }
    }

    private fun getVehicleData() {
        loading()
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
                            composeData(data, reservation)
                        }

                        runOnUiThread {
                            loaded()
                            if (reservation.status == Status.FINALIZED) {
                                binding.buttonDetails.visibility = View.GONE
                            }
                        }
                    }

                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        finish()
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
                    finish()
                    Toast.makeText(
                        this@ReservationsDetailsActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun composeData(data: Car, reservation: Reservation) {
        binding.textDaily.text = "DIÁRIA: R$ " + data.value.toString()
        binding.textTotal.text =
            "TOTAL: R$ " + reservation.total_value.toString()
        binding.textWithdrawalDetails.text =
            "RETIRADA: " + dateFormatter(reservation.pickup)
        binding.textDevolutionDetails.text =
            "DEVOLUÇÃO: " + dateFormatter(reservation.devolution)
        binding.textStatusDetails.text = "STATUS: " + reservation.status
        binding.textStepDetails.text = "STEP: " + reservation.step
        binding.textColorDetails.text = "COR: " + data.color
        binding.textPlateDetails.text = "PLACA: " + data.plate

        binding.textNameCarDetails.text = data.brand + " " + data.model
        binding.textPriceCar.text = "R$ " + data.value.toString()
    }

    private fun dateFormatter(dataString: String): String {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return exitFormat.format(entryFormat.parse(dataString))
    }

    private fun loading() {
        binding.textDaily.visibility = View.GONE
        binding.textPriceCar.visibility = View.GONE
        binding.textNameCarDetails.visibility = View.GONE
        binding.textPlateDetails.visibility = View.GONE
        binding.textStepDetails.visibility = View.GONE
        binding.textColorDetails.visibility = View.GONE
        binding.textNameCarDetails.visibility = View.GONE
        binding.textDevolutionDetails.visibility = View.GONE
        binding.textWithdrawalDetails.visibility = View.GONE
        binding.textTotal.visibility = View.GONE
        binding.textStatusDetails.visibility = View.GONE
        binding.textTitleReservation.visibility = View.GONE
        binding.viewReservationsDetails.visibility = View.GONE
        binding.viewCarDetails.visibility = View.GONE
        binding.imageCarDetails.visibility = View.GONE
        binding.buttonDetails.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded() {
        binding.textDaily.visibility = View.VISIBLE
        binding.textPriceCar.visibility = View.VISIBLE
        binding.textNameCarDetails.visibility = View.VISIBLE
        binding.textPlateDetails.visibility = View.VISIBLE
        binding.textStepDetails.visibility = View.VISIBLE
        binding.textColorDetails.visibility = View.VISIBLE
        binding.textNameCarDetails.visibility = View.VISIBLE
        binding.textDevolutionDetails.visibility = View.VISIBLE
        binding.textWithdrawalDetails.visibility = View.VISIBLE
        binding.textTotal.visibility = View.VISIBLE
        binding.textStatusDetails.visibility = View.VISIBLE
        binding.textTitleReservation.visibility = View.VISIBLE
        binding.viewReservationsDetails.visibility = View.VISIBLE
        binding.viewCarDetails.visibility = View.VISIBLE
        binding.imageCarDetails.visibility = View.VISIBLE
        binding.buttonDetails.visibility = View.VISIBLE

        binding.progressBar.visibility = View.GONE
    }

    fun onDeleteCarClick(reservation: Reservation) {
        showDeleteConfirmationDialog(reservation)
    }

    private fun showDeleteConfirmationDialog(reservation: Reservation) {
        val dialog = Dialog(this)
        val dialogBinding: ModalLayoutBinding = ModalLayoutBinding.inflate(layoutInflater)
        val dialogView = dialogBinding.root
        dialog.setContentView(dialogView)

        dialogBinding.btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnConfirmar.setOnClickListener {
            deleteItem(reservation)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteItem(reservation: Reservation) {
        loading()
        val preferencesManager = UserPreferencesManager(this)
        val apiService = APIService(preferencesManager.getToken())
        val url = "/reservation?id=${reservation.id}"

        apiService.deleteData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@ReservationsDetailsActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    }
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
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
                    loaded()
                    Toast.makeText(
                        this@ReservationsDetailsActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
