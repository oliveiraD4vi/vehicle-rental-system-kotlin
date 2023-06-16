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
import com.example.projectmobile.api.types.Step
import com.example.projectmobile.databinding.FragmentReservationsBinding
import com.example.projectmobile.ui.auth.LoginActivity
import com.example.projectmobile.ui.cars.CarsFragment
import com.example.projectmobile.ui.formreservation.data.FormReservationDataActivity
import com.example.projectmobile.ui.formreservation.payment.FormReservationPaymentActivity
import com.example.projectmobile.ui.formreservation.vehicle.FormReservationVehicleActivity
import com.example.projectmobile.ui.reservations.adapter.ReservationsAdapter
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class ReservationsFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentReservationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesManager: UserPreferencesManager
    private lateinit var adapter: ReservationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationsBinding.inflate(inflater, container, false)

        preferencesManager = UserPreferencesManager(requireContext())

        adapter = ReservationsAdapter { reservation ->
            preferencesManager.saveSelectedReservation(reservation)
            startActivity(Intent(requireContext(), ReservationsDetailsActivity::class.java))
        }

        // layout
        binding.recyclerReservations.layoutManager = LinearLayoutManager(context)

        //adapter
        binding.recyclerReservations.adapter = adapter

        if (verifyUserRole(preferencesManager)) {
            getLast(preferencesManager, adapter)
        } else {
            loadedWithZero()
        }

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

    private fun getLast(
        preferencesManager: UserPreferencesManager,
        adapter: ReservationsAdapter
    ) {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val id = preferencesManager.getUserId()
        val url = "/reservation/last?id=$id"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val reservation: Reservation = response.reservation
                    preferencesManager.saveVehicleId(reservation.vehicle_id.toString())
                    preferencesManager.saveReservationId(reservation.id.toString())

                    when (reservation.step) {
                        Step.PERSONAL ->
                            startActivity(
                                Intent(
                                    requireContext(),
                                    FormReservationDataActivity::class.java
                                )
                            )
                        Step.VEHICLE ->
                            startActivity(
                                Intent(
                                    requireContext(),
                                    FormReservationVehicleActivity::class.java
                                )
                            )
                        else ->
                            startActivity(
                                Intent(
                                    requireContext(),
                                    FormReservationPaymentActivity::class.java
                                )
                            )
                    }
                } else {
                    activity?.runOnUiThread {
                        getReservations(preferencesManager, adapter)
                    }
                }
            }

            override fun onError(error: IOException) {
                activity?.runOnUiThread {
                    getReservations(preferencesManager, adapter)
                }
            }
        })
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

    private fun loadedWithZero() {
        binding.recyclerReservations.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        if (preferencesManager.isLoggedIn()) {
            binding.buttonReservationsNew.visibility = View.VISIBLE
        } else {
            binding.buttonReservationsNew.visibility = View.GONE
        }

        binding.notFound.visibility = View.VISIBLE
    }

    override fun onClick(view: View) {
        if (view.id == R.id.button_reservations_new) {
            val fragment = CarsFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
