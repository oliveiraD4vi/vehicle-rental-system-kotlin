package com.example.projectmobile.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.projectmobile.LoginActivity
import com.example.projectmobile.MainActivity
import com.example.projectmobile.RegisterActivity
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.UserData
import com.example.projectmobile.databinding.FragmentProfileBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val preferencesManager = UserPreferencesManager(requireContext())

        // Verify user and determine button
        verifyUserLoggedIn(preferencesManager)

        // Handle submit and edit button action
        submitActionHandler(preferencesManager)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun submitActionHandler(preferencesManager: UserPreferencesManager) {
        binding.editButton.setOnClickListener {
            enableEditTextFields()

            binding.loginButton.visibility = View.GONE
            binding.editButton.visibility = View.GONE
            binding.submitButton.visibility = View.VISIBLE
        }

        binding.submitButton.setOnClickListener {
            loading()
            sendDataToServer()
            disableEditTextFields()
        }
    }

    private fun sendDataToServer() {
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                loaded()
            }, 2000)
        }
    }

    private fun getUserData(preferencesManager: UserPreferencesManager) {
        loading()

        val apiService = APIService(preferencesManager.getToken())
        val userId = preferencesManager.getUserId()
        val url = "/user?id=$userId"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data: UserData? = response.user
                    if (data != null) {
                        preferencesManager.saveData(data)
                        showUserInfo(data)
                    }
                } else {
                    preferencesManager.logout()
                    startActivity(Intent(activity, MainActivity::class.java))
                    val errorCode = response.message.toString()

                    activity?.runOnUiThread {
                        loaded()
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
                    loaded()
                    Toast.makeText(
                        requireContext(),
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun verifyUserLoggedIn(preferencesManager: UserPreferencesManager) {
        val buttonLogin: Button = binding.loginButton
        val buttonRegister: Button = binding.signUpButton

        if (preferencesManager.isLoggedIn()) {
            buttonLogin.text = "Sair"

            val data = preferencesManager.getUserData()

            if (data != null) {
                showUserInfo(data)
            } else {
                getUserData(preferencesManager)
            }
        } else {
            showNotLoggedIn()
        }

        buttonLogin.setOnClickListener {
            var intent = Intent(activity, LoginActivity::class.java)

            if (preferencesManager.isLoggedIn()) {
                preferencesManager.logout()

                intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            startActivity(intent)
        }

        buttonRegister.setOnClickListener {
            var intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showUserInfo(userData: UserData) {
        activity?.runOnUiThread {
            loaded()
        }
        disableEditTextFields()

        binding.nameEditText.setText(userData.name)
        binding.phoneEditText.setText(userData.phone)
        binding.emailEditText.setText(userData.email)
        binding.cpfEditText.setText(userData.cpf)
        binding.birthdateEditText.setText(userData.bornAt)
        binding.streetEditText.setText(userData.street)
        binding.numberEditText.setText(userData.number.toString())
        binding.cityEditText.setText(userData.city)
        binding.neighborhoodEditText.setText(userData.neighborhood)
        binding.stateEditText.setText(userData.state)
        binding.countryEditText.setText(userData.country)
    }

    private fun showNotLoggedIn() {
        binding.address.visibility = View.GONE
        binding.personalInfoLayout.visibility = View.GONE
        binding.addressLayout.visibility = View.GONE
        binding.editButton.visibility = View.GONE

        binding.notLoggedInTextView.visibility = View.VISIBLE
        binding.signUpButton.visibility = View.VISIBLE

        binding.notLoggedInTextView.text = "Você não está logado"
    }

    private fun disableEditTextFields() {
        binding.nameEditText.isEnabled = false
        binding.phoneEditText.isEnabled = false
        binding.emailEditText.isEnabled = false
        binding.birthdateEditText.isEnabled = false
        binding.cpfEditText.isEnabled = false
        binding.streetEditText.isEnabled = false
        binding.numberEditText.isEnabled = false
        binding.cityEditText.isEnabled = false
        binding.neighborhoodEditText.isEnabled = false
        binding.stateEditText.isEnabled = false
        binding.countryEditText.isEnabled = false
    }

    private fun enableEditTextFields() {
        binding.nameEditText.isEnabled = true
        binding.phoneEditText.isEnabled = true
        binding.emailEditText.isEnabled = true
        binding.birthdateEditText.isEnabled = true
        binding.cpfEditText.isEnabled = true
        binding.streetEditText.isEnabled = true
        binding.numberEditText.isEnabled = true
        binding.cityEditText.isEnabled = true
        binding.neighborhoodEditText.isEnabled = true
        binding.stateEditText.isEnabled = true
        binding.countryEditText.isEnabled = true
    }

    private fun loading() {
        binding.progressBar.visibility = View.VISIBLE

        binding.loginButton.visibility = View.GONE
        binding.personalInfoLayout.visibility = View.GONE
        binding.address.visibility = View.GONE
        binding.addressLayout.visibility = View.GONE
        binding.submitButton.visibility = View.GONE
    }

    private fun loaded() {
        binding.loginButton.visibility = View.VISIBLE
        binding.personalInfoLayout.visibility = View.VISIBLE
        binding.addressLayout.visibility = View.VISIBLE
        binding.address.visibility = View.VISIBLE
        binding.editButton.visibility = View.VISIBLE

        binding.progressBar.visibility = View.GONE
    }
}
