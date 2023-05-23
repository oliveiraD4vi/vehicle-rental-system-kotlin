package com.example.projectmobile.ui.cars

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmobile.MainActivity
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.FragmentCarsBinding
import com.example.projectmobile.api.types.Cars
import com.example.projectmobile.ui.cars.adapter.CarsAdapter
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class CarsFragment : Fragment() {
    private var _binding: FragmentCarsBinding? = null
    private val binding get() = _binding!!
    private val adapter = CarsAdapter()

    private lateinit var carsViewModel: CarsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val preferencesManager = UserPreferencesManager(requireContext())
        carsViewModel = ViewModelProvider(this)[CarsViewModel::class.java]
        _binding = FragmentCarsBinding.inflate(inflater, container, false)

        //layout
        binding.recyclerCars.layoutManager = LinearLayoutManager(context)

        //adapter
        binding.recyclerCars.adapter = adapter

        getAll(preferencesManager)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getAll(preferencesManager: UserPreferencesManager){
        val apiService = APIService()
        val url = "/vehicle/list?page=1&size=15&sort=ASC&search="
        var listCar: List<Cars> = listOf()

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val carsListApi: List<Cars>? = response.vehicles
                    if (carsListApi != null) {
                        activity?.runOnUiThread {
                            adapter.updatedCars(carsListApi)
                        }
                    }
                } else {
                    preferencesManager.logout()
                    startActivity(Intent(activity, MainActivity::class.java))
                    val errorCode = response.message

                    activity?.runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                preferencesManager.logout()
                startActivity(Intent(activity, MainActivity::class.java))

                activity?.runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
