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
    }

    override fun onClick(view: View) {
        if (view.id == R.id.returnButton) {
            finish()
        } else if (view.id == R.id.button_cancel_payment_form) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if (view.id == R.id.button_confirm_payment_form) {
            startActivity(Intent(this, MapViewActivity::class.java))
            finish()
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
                    val intent = Intent(this@FormReservationPaymentActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    val errorCode = response.message

                    runOnUiThread {
//                    loaded()
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
//                    loaded()
                    Toast.makeText(
                        this@FormReservationPaymentActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
