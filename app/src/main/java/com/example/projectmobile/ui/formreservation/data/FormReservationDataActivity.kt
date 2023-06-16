package com.example.projectmobile.ui.formreservation.data

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
import com.example.projectmobile.api.types.User
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

        getUserData()

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
                confirmData()
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
        loading()
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
                        loaded()
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
                    loaded()
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

    private fun getUserData() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val userId = preferencesManager.getUserId()
        val url = "/user/personal?id=$userId"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val user: User? = response.user

                    runOnUiThread {
                        binding.editPhone.setText(user?.phone)
                        binding.editRoad.setText(user?.street)
                        binding.editNumber.setText(user?.number.toString())
                        binding.editNeighborhood.setText(user?.neighborhood)
                        binding.editCity.setText(user?.city)
                        binding.editState.setText(user?.state)
                        binding.editCountry.setText(user?.country)

                        loaded()
                    }
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
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
                    loaded()
                    Toast.makeText(
                        this@FormReservationDataActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun confirmData() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/user"

        val requestData = getRequestData()

        apiService.putData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    runOnUiThread {
                        Toast.makeText(
                            this@FormReservationDataActivity,
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
                            this@FormReservationDataActivity,
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
                        this@FormReservationDataActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun getRequestData(): String {
        val userId = preferencesManager.getUserId()
        val phone = binding.editPhone.text.toString()
        val street = binding.editRoad.text.toString()
        val number = binding.editNumber.text.toString()
        val neighborhood = binding.editNeighborhood.text.toString()
        val city = binding.editCity.text.toString()
        val state = binding.editState.text.toString()
        val country = binding.editCountry.text.toString()

        return "{\"id\": $userId, " +
                "\"phone\": \"$phone\", " +
                "\"street\": \"$street\", " +
                "\"number\": $number, " +
                "\"neighborhood\": \"$neighborhood\", " +
                "\"state\": \"$state\", " +
                "\"city\": \"$city\", " +
                "\"country\": \"$country\"}"
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
                            this@FormReservationDataActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    startActivity(Intent(this@FormReservationDataActivity, FormReservationVehicleActivity::class.java))
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
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
                    loaded()
                    Toast.makeText(
                        this@FormReservationDataActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun loading() {
        binding.editPhone.visibility = View.GONE
        binding.textAddress.visibility = View.GONE
        binding.editRoad.visibility = View.GONE
        binding.editNumber.visibility = View.GONE
        binding.editNeighborhood.visibility = View.GONE
        binding.editCity.visibility = View.GONE
        binding.editState.visibility = View.GONE
        binding.editCountry.visibility = View.GONE
        binding.buttonNextDataForm.visibility = View.GONE
        binding.buttonCancelDataForm.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded() {
        binding.editPhone.visibility = View.VISIBLE
        binding.textAddress.visibility = View.VISIBLE
        binding.editRoad.visibility = View.VISIBLE
        binding.editNumber.visibility = View.VISIBLE
        binding.editNeighborhood.visibility = View.VISIBLE
        binding.editCity.visibility = View.VISIBLE
        binding.editState.visibility = View.VISIBLE
        binding.editCountry.visibility = View.VISIBLE
        binding.buttonNextDataForm.visibility = View.VISIBLE
        binding.buttonCancelDataForm.visibility = View.VISIBLE

        binding.progressBar.visibility = View.GONE
    }
}
