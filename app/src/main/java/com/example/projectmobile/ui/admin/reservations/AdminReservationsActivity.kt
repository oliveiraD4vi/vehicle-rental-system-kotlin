package com.example.projectmobile.ui.admin.reservations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.ActivityAdminCarsBinding
import com.example.projectmobile.databinding.ActivityAdminReservationsBinding
import com.example.projectmobile.ui.admin.reservations.adapter.AdminReservationAdapter
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class AdminReservationsActivity : AppCompatActivity() {
    private var _binding: ActivityAdminReservationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminReservationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val adapter = AdminReservationAdapter()

        //adapter
        binding.recyclerReservations.adapter = adapter

        //layout
        binding.recyclerReservations.layoutManager = LinearLayoutManager(this)

        binding.returnButton.setOnClickListener {
            finish()
        }

        handleSearch("", adapter)

        binding.imageSearch.setOnClickListener {
            val string = binding.editResearch.text.toString()
            handleSearch(string, adapter)
        }
    }

    private fun handleSearch(search: String, adapter: AdminReservationAdapter) {
        val preferencesManager = UserPreferencesManager(this)
        getReservationsData(preferencesManager, search, adapter)
    }

    private fun getReservationsData(
        preferencesManager: UserPreferencesManager,
        search: String,
        adapter: AdminReservationAdapter
    ) {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/reservation/list?page=1&size=100&sort=ASC&search=$search"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data = response.reservations
                    if (data != null) {
                        runOnUiThread {
                            adapter.updateReservations(data)
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
                            this@AdminReservationsActivity,
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
                        this@AdminReservationsActivity,
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
        binding.recyclerReservations.visibility = View.GONE
    }

    private fun loaded() {
        binding.progressBar.visibility = View.GONE

        binding.notFound.visibility = View.GONE
        binding.recyclerReservations.visibility = View.VISIBLE
    }

    private fun loadedWithZero() {
        binding.progressBar.visibility = View.GONE

        binding.notFound.visibility = View.VISIBLE
        binding.recyclerReservations.visibility = View.GONE
    }
}
