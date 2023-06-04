package com.example.projectmobile.ui.admin.reservations.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.Reservation
import com.example.projectmobile.databinding.ActivityCreateReservationBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ManageReservationActivity : AppCompatActivity() {
    private var reservationId: Int? = null
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

        verifySelectedItem()

        binding.editButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableFields()

                binding.registerButton.visibility = View.GONE
                binding.saveButton.visibility = View.VISIBLE
            } else {
                disableFields()
                binding.saveButton.visibility = View.GONE
            }
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

        binding.saveButton.setOnClickListener {
//            saveData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferencesManager.removeSelectedReservation()
    }

    private fun verifySelectedItem() {
        val item: Reservation? = preferencesManager.getSelectedReservation()

        if (item != null) {
            reservationId = item.id
            binding.titleTextView.text = "ID: $reservationId"
            binding.userId.setText(item.user_id.toString())
            binding.vehicleId.setText(item.vehicle_id.toString())
            binding.pickup.setText(dateFormatter(item.pickup))
            binding.devolution.setText(dateFormatter(item.devolution))

            binding.editButton.visibility = View.VISIBLE
            binding.registerButton.visibility = View.GONE

            disableFields()
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
        val url = "/reservation"

        val requestData = getRequestData()

        println(requestData)

        apiService.postData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@ManageReservationActivity,
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
                            this@ManageReservationActivity,
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
                        this@ManageReservationActivity,
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

        return "{\"user_id\": \"$userId\", " +
                "\"vehicle_id\": \"$vehicleId\", " +
                "\"pickup\": \"$pickup\", " +
                "\"devolution\": \"$devolution\"}"
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

    private fun disableFields() {
        binding.userId.isEnabled = false
        binding.vehicleId.isEnabled = false
        binding.pickup.isEnabled = false
        binding.devolution.isEnabled = false
    }

    private fun enableFields() {
        binding.userId.isEnabled = true
        binding.vehicleId.isEnabled = true
        binding.pickup.isEnabled = true
        binding.devolution.isEnabled = true
    }

    private fun dateFormatter(dataString: String): String? {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return entryFormat.parse(dataString)?.let { exitFormat.format(it) }
    }
}
