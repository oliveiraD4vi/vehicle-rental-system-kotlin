package com.example.projectmobile.ui.formreservation.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.projectmobile.MainActivity
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.ActivityFormReservationPaymentBinding
import com.example.projectmobile.ui.formreservation.map.MapViewActivity
import com.example.projectmobile.ui.formreservation.vehicle.FormReservationVehicleActivity
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class FormReservationPaymentActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFormReservationPaymentBinding
    private lateinit var preferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormReservationPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        preferencesManager = UserPreferencesManager(this)

        binding.returnButton.setOnClickListener(this)
        binding.buttonCancelPaymentForm.setOnClickListener(this)
        binding.buttonConfirmPaymentForm.setOnClickListener(this)
        binding.buttonLeaveForm.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.returnButton -> {
                previous()
            }
            R.id.button_cancel_payment_form -> {
                deleteReservation()
            }
            R.id.button_confirm_payment_form -> {
                next()
            }
            R.id.button_leave_form -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    private fun deleteReservation() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val reservationId = preferencesManager.getReservationId()
        val url = "/reservation?id=$reservationId"

        apiService.deleteData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val intent =
                        Intent(this@FormReservationPaymentActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
                        Toast.makeText(
                            this@FormReservationPaymentActivity,
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
                        this@FormReservationPaymentActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun next() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val reservationId = preferencesManager.getReservationId()
        val url = "/reservation/next?id=$reservationId"

        apiService.putData(url, "", object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    runOnUiThread {
                        Toast.makeText(
                            this@FormReservationPaymentActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    startActivity(
                        Intent(
                            this@FormReservationPaymentActivity,
                            MapViewActivity::class.java
                        )
                    )
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
                        Toast.makeText(
                            this@FormReservationPaymentActivity,
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
                        this@FormReservationPaymentActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun previous() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val reservationId = preferencesManager.getReservationId()
        val url = "/reservation/previous?id=$reservationId"

        apiService.putData(url, "", object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    runOnUiThread {
                        Toast.makeText(
                            this@FormReservationPaymentActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    startActivity(
                        Intent(
                            this@FormReservationPaymentActivity,
                            FormReservationVehicleActivity::class.java
                        )
                    )
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
                        Toast.makeText(
                            this@FormReservationPaymentActivity,
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
                        this@FormReservationPaymentActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun loading() {
        binding.textView.visibility = View.GONE
        binding.buttonCancelPaymentForm.visibility = View.GONE
        binding.buttonConfirmPaymentForm.visibility = View.GONE
        binding.buttonLeaveForm.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded() {
        binding.textView.visibility = View.VISIBLE
        binding.buttonCancelPaymentForm.visibility = View.VISIBLE
        binding.buttonConfirmPaymentForm.visibility = View.VISIBLE
        binding.buttonLeaveForm.visibility = View.VISIBLE

        binding.progressBar.visibility = View.GONE
    }
}
