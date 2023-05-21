package com.example.projectmobile.ui.cars

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.MainActivity
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.FragmentCarsBinding
import com.example.projectmobile.model.Cars
import com.example.projectmobile.ui.cars.adapter.CarsAdapter
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class CarsFragment : Fragment() {

    private var _binding: FragmentCarsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter = CarsAdapter()
    private lateinit var carsViewModel: CarsViewModel
    var carsList = ArrayList<Cars>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val preferencesManager = UserPreferencesManager(requireContext())
        carsViewModel =
            ViewModelProvider(this).get(CarsViewModel::class.java)

        _binding = FragmentCarsBinding.inflate(inflater, container, false)

        //layout
        binding.recyclerCars.layoutManager = LinearLayoutManager(context)

        //adapter
        binding.recyclerCars.adapter = adapter

        getAll(preferencesManager)

        adapter.updatedCars(carsList)

        observe()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe(){
        carsViewModel.carsList.observe(viewLifecycleOwner) {
            adapter.updatedCars(it)
        }
    }

    private fun getAll(preferencesManager: UserPreferencesManager){
        val apiService = APIService()
        val url = "/vehicle/list"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    var carsListApi: ArrayList<Cars>? = response.vehicles
                    if (carsListApi != null) {
                        for(i in carsListApi){
                            carsList.add(i)
                        }
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