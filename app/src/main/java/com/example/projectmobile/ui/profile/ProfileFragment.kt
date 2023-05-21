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
        binding.nameTextView.text = "Nome: Isadora Oliveira"
        binding.emailTextView.text = "Email: isadora@email.com"
        binding.cpfTextView.text = "CPF: 445.444.565-92"
        binding.birthdateTextView.text = "Data de Nascimento: 05/04/2001"
        binding.streetTextView.text = "Rua: Laerte Pinheiro"
        binding.numberTextView.text = "Número: 47"
        binding.cityTextView.text = "Cidade: Quixadá"
        binding.neighborhoodTextView.text = "Bairro: Centro"
        binding.stateTextView.text = "Estado: CE"
        binding.countryTextView.text = "País: Brasil"
    }

    private fun showNotLoggedIn() {
        binding.nameTextView.visibility = View.GONE
        binding.emailTextView.visibility = View.GONE
        binding.cpfTextView.visibility = View.GONE
        binding.birthdateTextView.visibility = View.GONE
        binding.streetTextView.visibility = View.GONE
        binding.numberTextView.visibility = View.GONE
        binding.cityTextView.visibility = View.GONE
        binding.neighborhoodTextView.visibility = View.GONE
        binding.stateTextView.visibility = View.GONE
        binding.countryTextView.visibility = View.GONE
        binding.personalInfo.visibility = View.GONE
        binding.address.visibility = View.GONE

        binding.notLoggedInTextView.visibility = View.VISIBLE
        binding.signUpButton.visibility = View.VISIBLE

        binding.notLoggedInTextView.text = "Você não está logado"
    }
}
