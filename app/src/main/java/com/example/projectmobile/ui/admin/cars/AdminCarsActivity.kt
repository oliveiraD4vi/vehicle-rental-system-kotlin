package com.example.projectmobile.ui.admin.cars

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.ActivityAdminCarsBinding
import com.example.projectmobile.ui.admin.cars.adapter.AdminCarsAdapter
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class AdminCarsActivity : AppCompatActivity() {
    private var _binding: ActivityAdminCarsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminCarsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val adapter = AdminCarsAdapter()

        //adapter
        binding.recyclerCars.adapter = adapter

        //layout
        binding.recyclerCars.layoutManager = LinearLayoutManager(this)

        binding.returnButton.setOnClickListener {
            finish()
        }

        handleSearch("", adapter)

        binding.imageSearch.setOnClickListener {
            val string = binding.editResearch.text.toString()
            handleSearch(string, adapter)
        }
    }

    private fun handleSearch(search: String, adapter: AdminCarsAdapter) {
        val preferencesManager = UserPreferencesManager(this)
        getVehiclesData(preferencesManager, search, adapter)
    }

    private fun getVehiclesData(
        preferencesManager: UserPreferencesManager,
        search: String,
        adapter: AdminCarsAdapter
    ) {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/vehicle/list?page=1&size=100&sort=ASC&search=$search"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data = response.vehicles
                    println(data)
                    if (data != null) {
                        runOnUiThread {
                            adapter.updateCars(data)
                        }
                    }

                    runOnUiThread {
                        loaded()
                    }
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loadedWithZero()
                        Toast.makeText(
                            this@AdminCarsActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    loadedWithZero()
                    Toast.makeText(
                        this@AdminCarsActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun loading() {
        binding.progressBar.visibility = View.VISIBLE

        binding.notFound.visibility = View.GONE
        binding.recyclerCars.visibility = View.GONE
    }

    private fun loaded() {
        binding.progressBar.visibility = View.GONE

        binding.notFound.visibility = View.GONE
        binding.recyclerCars.visibility = View.VISIBLE
    }

    private fun loadedWithZero() {
        binding.progressBar.visibility = View.GONE

        binding.notFound.visibility = View.VISIBLE
        binding.recyclerCars.visibility = View.GONE
    }
}
