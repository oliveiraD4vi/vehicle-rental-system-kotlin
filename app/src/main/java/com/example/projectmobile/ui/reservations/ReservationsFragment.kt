package com.example.projectmobile.ui.reservations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.Reservation
import com.example.projectmobile.databinding.FragmentReservationsBinding
import com.example.projectmobile.ui.auth.LoginActivity
import com.example.projectmobile.ui.formreservation.data.FormReservationDataActivity
import com.example.projectmobile.ui.reservations.adapter.ReservationsAdapter
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class ReservationsFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentReservationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationsBinding.inflate(inflater, container, false)

        val preferencesManager = UserPreferencesManager(requireContext())
        val cond: Boolean = verifyUserRole(preferencesManager)

        val adapter = ReservationsAdapter { reservation ->
            preferencesManager.saveSelectedReservation(reservation)
            startActivity(Intent(requireContext(), ReservationsDetailsActivity::class.java))
        }

        // layout
        binding.recyclerReservations.layoutManager = LinearLayoutManager(context)

        //adapter
        binding.recyclerReservations.adapter = adapter

        if (cond) getReservations(preferencesManager, adapter)

        binding.buttonReservationsNew.setOnClickListener(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun verifyUserRole(preferencesManager: UserPreferencesManager): Boolean {
        if (!preferencesManager.isLoggedIn()) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            return false
        }

        return true
    }

    private fun getReservations(
        preferencesManager: UserPreferencesManager,
        adapter: ReservationsAdapter
    ) {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val id = preferencesManager.getUserId()
        val url = "/reservation/user?id=$id"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val reservationsListApi: List<Reservation>? = response.reservations
                    if (reservationsListApi != null) {
                        activity?.runOnUiThread {
                            adapter.updatedReservations(reservationsListApi)
                        }
                    }

                    activity?.runOnUiThread {
                        loaded()
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

    private fun loading() {
        binding.recyclerReservations.visibility = View.GONE
        binding.buttonReservationsNew.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded() {
        binding.recyclerReservations.visibility = View.VISIBLE
        binding.buttonReservationsNew.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun onClick(view: View) {
        if (view.id == R.id.button_reservations_new) {
            val intent = Intent(requireContext(), FormReservationDataActivity::class.java)
            startActivity(intent)
        }
    }
}
