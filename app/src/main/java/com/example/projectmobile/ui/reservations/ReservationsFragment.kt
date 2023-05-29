package com.example.projectmobile.ui.reservations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmobile.LoginActivity
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.Cars
import com.example.projectmobile.api.types.Reservations
import com.example.projectmobile.databinding.FragmentReservationsBinding
import com.example.projectmobile.ui.cars.adapter.CarsAdapter
import com.example.projectmobile.ui.formreservation.data.FormReservationDataActivity
import com.example.projectmobile.ui.reservations.adapter.ReservationsAdapter
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class ReservationsFragment : Fragment() {
    private var _binding: FragmentReservationsBinding? = null
    private var reservationsList: List<Reservations> = listOf()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationsBinding.inflate(inflater, container, false)

        val preferencesManager = UserPreferencesManager(requireContext())
        verifyUserRole(preferencesManager)

        val adapter = ReservationsAdapter{ reservation ->
        }

        binding.recyclerReservations.layoutManager = LinearLayoutManager(context)

        //adapter
        binding.recyclerReservations.adapter = adapter

        getReservations(preferencesManager, adapter)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun verifyUserRole(preferencesManager: UserPreferencesManager) {
        if (!preferencesManager.isLoggedIn()) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getReservations(preferencesManager: UserPreferencesManager, adapter: ReservationsAdapter){
        val apiService = APIService(preferencesManager.getToken())
        val id = preferencesManager.getUserId()
        val url: String = "/reservation/user?id=$id"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val reservationsListApi: List<Reservations>? = response.reservations
                    if (reservationsListApi != null) {
                        activity?.runOnUiThread {
                            adapter.updatedReservations(reservationsListApi)
                        }
                    }
                } else {
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