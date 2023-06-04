package com.example.projectmobile.ui.admin.cars.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.Car
import com.example.projectmobile.databinding.ActivityCreateCarBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class ManageCarActivity : AppCompatActivity() {
    private var vehicleId: Int? = null
    private var _binding: ActivityCreateCarBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateCarBinding.inflate(layoutInflater)
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
                loaded(true)
                binding.saveButton.visibility = View.GONE
            }
        }

        binding.registerButton.setOnClickListener {
            val brand = binding.editBrand.text.toString()
            val model = binding.editModel.text.toString()
            val color = binding.editColor.text.toString()
            val plate = binding.editPlate.text.toString()
            val price = binding.editPrice.text.toString()

            if (validateFields(brand, model, color, plate, price)) {
                sendDataToServer()
            }
        }

        binding.saveButton.setOnClickListener {
            val brand = binding.editBrand.text.toString()
            val model = binding.editModel.text.toString()
            val color = binding.editColor.text.toString()
            val plate = binding.editPlate.text.toString()
            val price = binding.editPrice.text.toString()

            if (validateFields(brand, model, color, plate, price)) {
                saveData()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferencesManager.removeSelectedCar()
    }

    private fun verifySelectedItem() {
        val item: Car? = preferencesManager.getSelectedCar()

        if (item != null) {
            vehicleId = item.id
            binding.titleTextView.text = "ID: $vehicleId"
            binding.editBrand.setText(item.brand)
            binding.editPrice.setText(item.value.toString())
            binding.editPlate.setText(item.plate)
            binding.editColor.setText(item.color)
            binding.editModel.setText(item.model)

            binding.editButton.visibility = View.VISIBLE
            binding.registerButton.visibility = View.GONE

            disableFields()
        }
    }

    private fun validateFields(
        brand: String,
        model: String,
        color: String,
        plate: String,
        price: String,
    ): Boolean {
        if (TextUtils.isEmpty(brand)) {
            binding.editBrand.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(model)) {
            binding.editModel.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(color)) {
            binding.editColor.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(plate)) {
            binding.editPlate.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(price)) {
            binding.editPrice.error = "Campo obrigatório"
            return false
        }

        return true
    }

    private fun sendDataToServer() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/vehicle/register"

        val requestData = getRequestData()

        apiService.postData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@ManageCarActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    finish()
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded(false)
                        Toast.makeText(
                            this@ManageCarActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    loaded(false)
                    Toast.makeText(
                        this@ManageCarActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun saveData() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/vehicle"

        val requestData = getRequestData()

        println(requestData)

        apiService.putData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@ManageCarActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    finish()
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded(true)
                        Toast.makeText(
                            this@ManageCarActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    loaded(true)
                    Toast.makeText(
                        this@ManageCarActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun getRequestData(): String {
        val brand = binding.editBrand.text.toString()
        val model = binding.editModel.text.toString()
        val color = binding.editColor.text.toString()
        val plate = binding.editPlate.text.toString()
        val price = binding.editPrice.text.toString()

        return "{\"id\": $vehicleId, " +
                "\"brand\": \"$brand\", " +
                "\"model\": \"$model\", " +
                "\"color\": \"$color\", " +
                "\"plate\": \"$plate\", " +
                "\"value\": $price}"
    }

    private fun loading() {
        binding.titleTextView.visibility = View.GONE
        binding.editBrand.visibility = View.GONE
        binding.editModel.visibility = View.GONE
        binding.editColor.visibility = View.GONE
        binding.editPlate.visibility = View.GONE
        binding.editPrice.visibility = View.GONE
        binding.registerButton.visibility = View.GONE
        binding.saveButton.visibility = View.GONE
        binding.editButton.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded(save: Boolean) {
        binding.titleTextView.visibility = View.VISIBLE
        binding.editBrand.visibility = View.VISIBLE
        binding.editModel.visibility = View.VISIBLE
        binding.editColor.visibility = View.VISIBLE
        binding.editPlate.visibility = View.VISIBLE
        binding.editPrice.visibility = View.VISIBLE

        if (!save) {
            binding.registerButton.visibility = View.VISIBLE
        } else {
            binding.saveButton.visibility = View.VISIBLE
            binding.editButton.visibility = View.VISIBLE
        }

        binding.progressBar.visibility = View.GONE
    }

    private fun disableFields() {
        binding.editModel.isEnabled = false
        binding.editBrand.isEnabled = false
        binding.editColor.isEnabled = false
        binding.editPlate.isEnabled = false
        binding.editPrice.isEnabled = false
    }

    private fun enableFields() {
        binding.editModel.isEnabled = true
        binding.editBrand.isEnabled = true
        binding.editColor.isEnabled = true
        binding.editPlate.isEnabled = true
        binding.editPrice.isEnabled = true
    }
}
