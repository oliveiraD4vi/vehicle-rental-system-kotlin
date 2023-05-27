package com.example.projectmobile.ui.reservations

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectmobile.LoginActivity
import com.example.projectmobile.MainActivity
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.Cars
import com.example.projectmobile.api.types.Reservation
import com.example.projectmobile.databinding.FragmentReservationsBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class ReservationsFragment : Fragment() {
    private var _binding: FragmentReservationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val preferencesManager = UserPreferencesManager(requireContext())
        verifyUserRole(preferencesManager)
        _binding = FragmentReservationsBinding.inflate(inflater, container, false)

        getReservations(preferencesManager)

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

    private fun getReservations(preferencesManager: UserPreferencesManager){
        val apiService = APIService()
        val id: String = preferencesManager.getUserId().toString()
        val url: String = "/reservation/user?id=3"
        println(id)
        println(url)
        println(preferencesManager.getToken())

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val reservationListApi: List<Reservation>? = response.reservation
                    println(reservationListApi)
                    if (reservationListApi != null) {
                        activity?.runOnUiThread {

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