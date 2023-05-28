package com.example.projectmobile.ui.cars

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.FragmentCarsBinding
import com.example.projectmobile.api.types.Car
import com.example.projectmobile.ui.cars.adapter.CarsAdapter
import com.example.projectmobile.ui.formreservation.data.FormReservationDataActivity
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class CarsFragment : Fragment() {
    private var _binding: FragmentCarsBinding? = null
    private val binding get() = _binding!!

    private lateinit var carsViewModel: CarsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        carsViewModel = ViewModelProvider(this)[CarsViewModel::class.java]
        _binding = FragmentCarsBinding.inflate(inflater, container, false)

        val preferencesManager = UserPreferencesManager(requireContext())

        // Listener function to car click
        val adapter = CarsAdapter { car ->
            preferencesManager.saveSelectedCar(car)
            startActivity(Intent(requireContext(), FormReservationDataActivity::class.java))
        }

        //layout
        binding.recyclerCars.layoutManager = LinearLayoutManager(context)

        //adapter
        binding.recyclerCars.adapter = adapter

        handleSearch(adapter)

        binding.imageSearch.setOnClickListener {
            handleSearch(adapter)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.editResearch.setText("")
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        binding.editResearch.setText("")
    }

    private fun getAll(adapter: CarsAdapter, url: String){
        loading()
        val apiService = APIService()

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val carsListApi: List<Car>? = response.vehicles
                    if (carsListApi != null) {
                        activity?.runOnUiThread {
                            adapter.updatedCars(carsListApi)
                        }
                    }

                    activity?.runOnUiThread {
                        Looper.myLooper()?.let {
                            Handler(it).postDelayed({
                                loaded()
                            }, 300)
                        }
                    }
                } else {
                    val errorCode = response.message

                    activity?.runOnUiThread {
                        loadedWithZero()

                        Toast.makeText(
                            requireContext(),
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                activity?.runOnUiThread {
                    loadedWithZero()

                    Toast.makeText(
                        requireContext(),
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun handleSearch(adapter: CarsAdapter){
        val search: String = binding.editResearch.text.toString()
        val url = "/vehicle/list?page=1&size=100&sort=ASC&search=$search"
        getAll(adapter, url)
    }

    private fun loading() {
        binding.notFound.visibility = View.GONE
        binding.recyclerCars.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded() {
        binding.notFound.visibility = View.GONE
        binding.recyclerCars.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun loadedWithZero() {
        binding.progressBar.visibility = View.GONE
        binding.notFound.visibility = View.VISIBLE
    }
}
