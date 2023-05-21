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
        verifyUserLoggedIn()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun verifyUserLoggedIn() {
        val preferencesManager = UserPreferencesManager(requireContext())
        val buttonLogin: Button = binding.buttonLogin

        if (preferencesManager.isLoggedIn()) {
            buttonLogin.text = "Sair"
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
    }
}
