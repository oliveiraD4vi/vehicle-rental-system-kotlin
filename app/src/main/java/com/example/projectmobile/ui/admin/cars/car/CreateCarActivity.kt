package com.example.projectmobile.ui.admin.cars.car

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.ActivityCreateCarBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class CreateCarActivity : AppCompatActivity() {
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
                            this@CreateCarActivity,
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
                            this@CreateCarActivity,
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
                        this@CreateCarActivity,
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

        return """
            "brand": "$brand",
            "model": "$model",
            "color": "$color",
            "plate": "$plate",
            "value": "$price",
        """.trimIndent()
    }

    private fun loading() {
        binding.titleTextView.visibility = View.GONE
        binding.editBrand.visibility = View.GONE
        binding.editModel.visibility = View.GONE
        binding.editColor.visibility = View.GONE
        binding.editPlate.visibility = View.GONE
        binding.editPrice.visibility = View.GONE
        binding.registerButton.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded() {
        binding.titleTextView.visibility = View.VISIBLE
        binding.editBrand.visibility = View.VISIBLE
        binding.editModel.visibility = View.VISIBLE
        binding.editColor.visibility = View.VISIBLE
        binding.editPlate.visibility = View.VISIBLE
        binding.editPrice.visibility = View.VISIBLE
        binding.registerButton.visibility = View.VISIBLE

        binding.progressBar.visibility = View.GONE
    }
}
