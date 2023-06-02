package com.example.projectmobile.ui.admin.reservations.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.ActivityCreateReservationBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class CreateReservationActivity : AppCompatActivity() {
    private var _binding: ActivityCreateReservationBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = UserPreferencesManager(this)

        supportActionBar?.hide()

        binding.returnButton.setOnClickListener {
            finish()
        }

        binding.registerButton.setOnClickListener {
            val userId = binding.userId.text.toString()
            val vehicleId = binding.vehicleId.text.toString()
            val pickup = binding.pickup.text.toString()
            val devolution = binding.devolution.text.toString()

            if (validateFields(userId, vehicleId, pickup, devolution)) {
                sendDataToServer()
            }
        }
    }

    private fun validateFields(
        userId: String,
        vehicleId: String,
        pickup: String,
        devolution: String,
    ): Boolean {
        if (TextUtils.isEmpty(userId)) {
            binding.userId.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(vehicleId)) {
            binding.vehicleId.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(pickup)) {
            binding.pickup.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(devolution)) {
            binding.devolution.error = "Campo obrigatório"
            return false
        }

        return true
    }

    private fun sendDataToServer() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/reservation/register"

        val requestData = getRequestData()

        apiService.postData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@CreateReservationActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    finish()
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
                        Toast.makeText(
                            this@CreateReservationActivity,
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
                        this@CreateReservationActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun getRequestData(): String {
        val userId = binding.userId.text.toString()
        val vehicleId = binding.vehicleId.text.toString()
        val pickup = binding.pickup.text.toString()
        val devolution = binding.devolution.text.toString()

        return """
            "userId": "$userId",
            "vehicleId": "$vehicleId",
            "pickup": "$pickup",
            "devolution": "$devolution",
        """.trimIndent()
    }

    private fun loading() {
        binding.titleTextView.visibility = View.GONE
        binding.idLayout.visibility = View.GONE
        binding.pickup.visibility = View.GONE
        binding.devolution.visibility = View.GONE
        binding.registerButton.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded() {
        binding.titleTextView.visibility = View.VISIBLE
        binding.idLayout.visibility = View.VISIBLE
        binding.pickup.visibility = View.VISIBLE
        binding.devolution.visibility = View.VISIBLE
        binding.registerButton.visibility = View.VISIBLE

        binding.progressBar.visibility = View.GONE
    }
}
