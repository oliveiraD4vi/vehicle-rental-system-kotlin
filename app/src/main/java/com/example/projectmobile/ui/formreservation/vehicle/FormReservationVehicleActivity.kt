package com.example.projectmobile.ui.formreservation.vehicle

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.example.projectmobile.MainActivity
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.ActivityFormReservationVehicleBinding
import com.example.projectmobile.ui.formreservation.data.FormReservationDataActivity
import com.example.projectmobile.ui.formreservation.payment.FormReservationPaymentActivity
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FormReservationVehicleActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {
    private var id: String = ""

    private lateinit var preferencesManager: UserPreferencesManager
    private lateinit var binding: ActivityFormReservationVehicleBinding

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormReservationVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        preferencesManager = UserPreferencesManager(this)
        // Verify selected vehicle
        verifySelectedCar(preferencesManager)
        // Verify if a date is already selected
        verifySelectedDate(preferencesManager)

        binding.buttonWithdrawalVehicleForm.setOnClickListener(this)
        binding.buttonDeliveryVehicleForm.setOnClickListener(this)
        binding.buttonCancelVehicleForm.setOnClickListener(this)
        binding.returnButton.setOnClickListener(this)
        binding.buttonNextVehicleForm.setOnClickListener(this)
        binding.buttonLeaveForm.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_withdrawal_vehicle_form -> {
                id = view.id.toString()
                handleDate()
            }
            R.id.button_delivery_vehicle_form -> {
                id = view.id.toString()
                handleDate()
            }
            R.id.button_cancel_vehicle_form -> {
                deleteReservation()
            }
            R.id.returnButton -> {
                previous()
            }
            R.id.button_next_vehicle_form -> {
                val dataWithdrawal: String = binding.buttonWithdrawalVehicleForm.text.toString()
                val dataDelivery: String = binding.buttonDeliveryVehicleForm.text.toString()
                handleContinue(dataWithdrawal, dataDelivery)
            }
            R.id.button_leave_form -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val preferencesManager = UserPreferencesManager(this)

        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dueDate = dateFormat.format(calendar.time)
        if (id == R.id.button_withdrawal_vehicle_form.toString()) {
            preferencesManager.saveWithdrawDate(dueDate)
            binding.buttonWithdrawalVehicleForm.text = dueDate
        } else if (id == R.id.button_delivery_vehicle_form.toString()) {
            preferencesManager.saveDeliveryDate(dueDate)
            binding.buttonDeliveryVehicleForm.text = dueDate
        }
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun handleContinue(dataWithdrawal: String, dataDelivery: String) {
        if (dataWithdrawal == "ESCOLHA UMA DATA" || dataDelivery == "ESCOLHA UMA DATA") {
            Toast.makeText(
                applicationContext,
                "As datas devem ser definidas!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            confirmVehicle()
        }
    }

    private fun verifySelectedCar(preferencesManager: UserPreferencesManager) {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val vehicleId = preferencesManager.getVehicleId()
        val url = "/vehicle?id=$vehicleId"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val car = response.vehicle

                    runOnUiThread {
                        if (car != null) {
                            binding.textNameCar.text = "${car.brand} ${car.model}"
                            binding.textPriceCar.text = "R$ ${car.value}"
                        }

                        loaded()
                    }
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
                        Toast.makeText(
                            this@FormReservationVehicleActivity,
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
                        this@FormReservationVehicleActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun verifySelectedDate(preferencesManager: UserPreferencesManager) {
        val wDate = preferencesManager.getWithdrawDate()
        if (wDate != null) {
            binding.buttonWithdrawalVehicleForm.text = wDate
        }

        val dDate = preferencesManager.getDeliveryDate()
        if (dDate != null) {
            binding.buttonDeliveryVehicleForm.text = dDate
        }
    }

    private fun confirmVehicle() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/reservation/confirm"

        val requestData = getRequestData()

        apiService.putData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    runOnUiThread {
                        Toast.makeText(
                            this@FormReservationVehicleActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    next()
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
                        Toast.makeText(
                            this@FormReservationVehicleActivity,
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
                        this@FormReservationVehicleActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun getRequestData(): String {
        val reservationId = preferencesManager.getReservationId()
        val vehicleId = preferencesManager.getVehicleId()
        val pickup = binding.buttonWithdrawalVehicleForm.text.toString()
        val devolution = binding.buttonDeliveryVehicleForm.text.toString()

        return "{\"reservationId\": \"$reservationId\", " +
                "\"vehicleId\": \"$vehicleId\", " +
                "\"pickup\": \"$pickup\", " +
                "\"devolution\": \"$devolution\"}"
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
                        Intent(this@FormReservationVehicleActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
                        Toast.makeText(
                            this@FormReservationVehicleActivity,
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
                        this@FormReservationVehicleActivity,
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
                            this@FormReservationVehicleActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    startActivity(
                        Intent(
                            this@FormReservationVehicleActivity,
                            FormReservationPaymentActivity::class.java
                        )
                    )
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
                        Toast.makeText(
                            this@FormReservationVehicleActivity,
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
                        this@FormReservationVehicleActivity,
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
                            this@FormReservationVehicleActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    startActivity(
                        Intent(
                            this@FormReservationVehicleActivity,
                            FormReservationDataActivity::class.java
                        )
                    )
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
                        Toast.makeText(
                            this@FormReservationVehicleActivity,
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
                        this@FormReservationVehicleActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun loading() {
        binding.view.visibility = View.GONE
        binding.textPriceCar.visibility = View.GONE
        binding.textNameCar.visibility = View.GONE
        binding.imageCar1.visibility = View.GONE
        binding.textDeliveryVehicleForm.visibility = View.GONE
        binding.textWithdrawalVehicleForm.visibility = View.GONE
        binding.buttonDeliveryVehicleForm.visibility = View.GONE
        binding.buttonWithdrawalVehicleForm.visibility = View.GONE
        binding.buttonNextVehicleForm.visibility = View.GONE
        binding.buttonCancelVehicleForm.visibility = View.GONE
        binding.buttonLeaveForm.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded() {
        binding.view.visibility = View.VISIBLE
        binding.textPriceCar.visibility = View.VISIBLE
        binding.textNameCar.visibility = View.VISIBLE
        binding.imageCar1.visibility = View.VISIBLE
        binding.textDeliveryVehicleForm.visibility = View.VISIBLE
        binding.textWithdrawalVehicleForm.visibility = View.VISIBLE
        binding.buttonDeliveryVehicleForm.visibility = View.VISIBLE
        binding.buttonWithdrawalVehicleForm.visibility = View.VISIBLE
        binding.buttonNextVehicleForm.visibility = View.VISIBLE
        binding.buttonCancelVehicleForm.visibility = View.VISIBLE
        binding.buttonLeaveForm.visibility = View.VISIBLE

        binding.progressBar.visibility = View.GONE
    }
}
