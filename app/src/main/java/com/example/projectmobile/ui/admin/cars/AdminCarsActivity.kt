package com.example.projectmobile.ui.admin.cars

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.Car
import com.example.projectmobile.databinding.ActivityAdminCarsBinding
import com.example.projectmobile.databinding.ModalLayoutBinding
import com.example.projectmobile.ui.admin.cars.adapter.AdminCarsAdapter
import com.example.projectmobile.ui.admin.cars.clicklistener.CarClickListener
import com.example.projectmobile.ui.admin.cars.manager.ManageCarActivity
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class AdminCarsActivity : AppCompatActivity(), CarClickListener {
    private var _binding: ActivityAdminCarsBinding? = null
    private val binding get() = _binding!!
    private val adapter = AdminCarsAdapter(this)

    private lateinit var preferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminCarsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = UserPreferencesManager(this)

        supportActionBar?.hide()

        //adapter
        binding.recyclerCars.adapter = adapter

        //layout
        binding.recyclerCars.layoutManager = LinearLayoutManager(this)

        binding.returnButton.setOnClickListener {
            finish()
        }

        binding.addButton.setOnClickListener {
            startActivity(Intent(this, ManageCarActivity::class.java))
        }

        handleSearch()

        binding.imageSearch.setOnClickListener {
            handleSearch()
        }
    }

    private fun handleSearch() {
        val search = binding.editResearch.text.toString()
        getVehiclesData(search)
    }

    private fun getVehiclesData(search: String) {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/vehicle/list?page=1&size=100&sort=ASC&search=$search"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data = response.vehicles
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

    override fun onEditCarClick(car: Car) {
        preferencesManager.saveSelectedCar(car)
        startActivity(Intent(this@AdminCarsActivity, ManageCarActivity::class.java))
    }

    override fun onDeleteCarClick(car: Car) {
        showDeleteConfirmationDialog(car)
    }

    private fun showDeleteConfirmationDialog(car: Car) {
        val dialog = Dialog(this)
        val dialogBinding: ModalLayoutBinding = ModalLayoutBinding.inflate(layoutInflater)
        val dialogView = dialogBinding.root
        dialog.setContentView(dialogView)

        dialogBinding.btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnConfirmar.setOnClickListener {
            deleteItem(car)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteItem(car: Car) {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/vehicle?id=${car.id}"

        apiService.deleteData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@AdminCarsActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()

                        handleSearch()
                    }
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
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
                    loaded()
                    Toast.makeText(
                        this@AdminCarsActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
