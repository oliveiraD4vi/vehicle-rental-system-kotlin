package com.example.projectmobile.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.projectmobile.ui.auth.LoginActivity
import com.example.projectmobile.MainActivity
import com.example.projectmobile.ui.auth.RegisterActivity
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.User
import com.example.projectmobile.databinding.FragmentProfileBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

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
        binding.editButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableEditTextFields()

                binding.loginButton.visibility = View.GONE
                binding.submitButton.visibility = View.VISIBLE
            } else {
                disableEditTextFields()
                binding.submitButton.visibility = View.GONE
                loaded()
            }
        }

        binding.submitButton.setOnClickListener {
            loading()
            sendDataToServer(preferencesManager)
            disableEditTextFields()
        }
    }

    private fun sendDataToServer(preferencesManager: UserPreferencesManager) {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/user"

        val requestData = getUserDataString(preferencesManager)

        println(requestData)

        apiService.putData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message
                    getUserData(preferencesManager)

                    activity?.runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val errorCode = response.message

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

    private fun getUserDataString(preferencesManager: UserPreferencesManager): String {
        val role = preferencesManager.getRole() ?: "CLIENT"
        val userId = preferencesManager.getUserId() ?: -1
        val personalDataId = preferencesManager.getUserData()?.personaldataId ?: -1

        val userData = User(
            bornAt = binding.birthdateEditText.text.toString(),
            city = binding.cityEditText.text.toString(),
            country = binding.countryEditText.text.toString(),
            cpf = binding.cpfEditText.text.toString(),
            email = binding.emailEditText.text.toString(),
            name = binding.nameEditText.text.toString(),
            neighborhood = binding.neighborhoodEditText.text.toString(),
            number = binding.numberEditText.text.toString().toInt(),
            phone = binding.phoneEditText.text.toString(),
            state = binding.stateEditText.text.toString(),
            street = binding.streetEditText.text.toString(),
            role = role,
            personaldataId = personalDataId,
            id = userId.toString().toInt(),
        )

        return "{\"bornAt\": \"${userData.bornAt}\", " +
                "\"city\": \"${userData.city}\", " +
                "\"country\": \"${userData.country}\", " +
                "\"cpf\": \"${userData.cpf}\", " +
                "\"email\": \"${userData.email}\", " +
                "\"id\": ${userData.id}, " +
                "\"name\": \"${userData.name}\", " +
                "\"neighborhood\": \"${userData.neighborhood}\", " +
                "\"number\": ${userData.number}, " +
                "\"personaldata_id\": ${userData.personaldataId}, " +
                "\"phone\": \"${userData.phone}\", " +
                "\"role\": \"${userData.role}\", " +
                "\"state\": \"${userData.state}\", " +
                "\"street\": \"${userData.street}\"}"
    }

    private fun getUserData(preferencesManager: UserPreferencesManager) {
        loading()

        val apiService = APIService(preferencesManager.getToken())
        val userId = preferencesManager.getUserId()
        val url = "/user/personal?id=$userId"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data: User? = response.user
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
        val buttonLogout: Button = binding.buttonLogout
        val buttonRegister: Button = binding.signUpButton

        if (preferencesManager.isLoggedIn()) {
            getUserData(preferencesManager)
        } else {
            showNotLoggedIn()
        }

        buttonLogin.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        buttonLogout.setOnClickListener {
            preferencesManager.logout()

            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        buttonRegister.setOnClickListener {
            var intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showUserInfo(userData: User) {
        disableEditTextFields()

        binding.nameEditText.setText(userData.name)
        binding.phoneEditText.setText(userData.phone)
        binding.emailEditText.setText(userData.email)
        binding.cpfEditText.setText(userData.cpf)
        binding.birthdateEditText.setText(dateFormatter(userData.bornAt))
        binding.streetEditText.setText(userData.street)
        binding.numberEditText.setText(userData.number.toString())
        binding.cityEditText.setText(userData.city)
        binding.neighborhoodEditText.setText(userData.neighborhood)
        binding.stateEditText.setText(userData.state)
        binding.countryEditText.setText(userData.country)

        binding.editButton.post {
            binding.editButton.isChecked = false
        }

        activity?.runOnUiThread {
            loaded()
        }
    }

    private fun showNotLoggedIn() {
        binding.buttonLogout.visibility = View.GONE
        binding.address.visibility = View.GONE
        binding.personalInfoLayout.visibility = View.GONE
        binding.addressLayout.visibility = View.GONE
        binding.editButton.visibility = View.GONE

        binding.notLoggedInTextView.visibility = View.VISIBLE
        binding.signUpButton.visibility = View.VISIBLE
        binding.notLoggedInTextView.visibility = View.VISIBLE
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

        binding.buttonLogout.visibility = View.GONE
        binding.loginButton.visibility = View.GONE
        binding.personalInfoLayout.visibility = View.GONE
        binding.address.visibility = View.GONE
        binding.addressLayout.visibility = View.GONE
        binding.submitButton.visibility = View.GONE
    }

    private fun loaded() {
        binding.buttonLogout.visibility = View.VISIBLE
        binding.personalInfoLayout.visibility = View.VISIBLE
        binding.addressLayout.visibility = View.VISIBLE
        binding.address.visibility = View.VISIBLE
        binding.editButton.visibility = View.VISIBLE

        binding.loginButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun dateFormatter(dataString: String): String {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return exitFormat.format(entryFormat.parse(dataString))
    }
}
