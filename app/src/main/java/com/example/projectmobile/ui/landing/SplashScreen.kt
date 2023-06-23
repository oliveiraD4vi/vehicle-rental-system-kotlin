package com.example.projectmobile.ui.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.projectmobile.MainActivity
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.ui.admin.AdminHomeActivity
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class SplashScreen : AppCompatActivity() {
    private lateinit var preferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        preferencesManager = UserPreferencesManager(this)

        verifyUserRole()
    }

    private fun verifyUserRole() {
        if (preferencesManager.isLoggedIn()) {
            check()
        } else {
            goToHome()
        }
    }

    private fun check() {
        val apiService = APIService(preferencesManager.getToken())
        val url = "/user/check"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    runOnUiThread {
                        if (preferencesManager.getRole() == "ADMIN") {
                            if (isBiometricSupported()) {
                                showBiometricPrompt()
                            } else {
                                preferencesManager.logout()
                                goToHome()
                            }
                        } else {
                            goToHome()
                        }
                    }
                } else {
                    preferencesManager.logout()
                    goToHome()

                    runOnUiThread {
                        Toast.makeText(
                            this@SplashScreen,
                            "Sessão expirada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                preferencesManager.logout()
                goToHome()

                runOnUiThread {
                    Toast.makeText(
                        this@SplashScreen,
                        "Sessão expirada",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE, BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE, BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                false
            }

            else -> {
                false
            }
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação Biométrica")
            .setSubtitle("Para continuar, aproxime sua digital do sensor")
            .setNegativeButtonText("Cancelar")
            .build()

        val biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    preferencesManager.logout()
                    goToHome()
                    showMessage("Você foi deslogado!")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    goToAdminHome()
                    showMessage("Autenticado com sucesso!")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    println("Failed")
                    showMessage("A autenticação falhou")
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun goToAdminHome() {
        val intent = Intent(this, AdminHomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
