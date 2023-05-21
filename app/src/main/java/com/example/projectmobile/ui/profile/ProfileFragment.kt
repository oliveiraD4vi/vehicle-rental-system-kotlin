package com.example.projectmobile.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.projectmobile.LoginActivity
import com.example.projectmobile.MainActivity
import com.example.projectmobile.RegisterActivity
import com.example.projectmobile.databinding.FragmentProfileBinding
import com.example.projectmobile.util.UserPreferencesManager

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

        // Verify user and determine button
        val preferencesManager = UserPreferencesManager(requireContext())
        verifyUserLoggedIn(preferencesManager)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun verifyUserLoggedIn(preferencesManager: UserPreferencesManager) {
        val buttonLogin: Button = binding.loginButton
        val buttonRegister: Button = binding.signUpButton

        if (preferencesManager.isLoggedIn()) {
            buttonLogin.text = "Sair"
            showUserInfo()
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

    private fun showUserInfo() {
        disableEditTextFields()
        binding.nameEditText.setText("Isadora Oliveira")
        binding.phoneEditText.setText("(88) 9297-3232")
        binding.emailEditText.setText("isadora@email.com")
        binding.cpfEditText.setText("445.444.565-92")
        binding.birthdateEditText.setText("05/04/2001")
        binding.streetEditText.setText("Laerte Pinheiro")
        binding.numberEditText.setText("47")
        binding.cityEditText.setText("Quixadá")
        binding.neighborhoodEditText.setText("Centro")
        binding.stateEditText.setText("CE")
        binding.countryEditText.setText("Brasil")
    }

    private fun showNotLoggedIn() {
        binding.address.visibility = View.GONE
        binding.personalInfoLayout.visibility = View.GONE
        binding.addressLayout.visibility = View.GONE

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
}
