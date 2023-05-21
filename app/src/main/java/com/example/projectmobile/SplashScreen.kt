package com.example.projectmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.ui.admin.AdminHomeActivity
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        // Verify if the user token is still valid
        val preferencesManager = UserPreferencesManager(this)
        verifyUserRole(preferencesManager)
    }

    private fun check(preferencesManager: UserPreferencesManager) {
        val apiService = APIService(preferencesManager.getToken())
        val url = "/user/check"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    if (preferencesManager.getRole() == "ADMIN") {
                        goToAdminHome()
                    } else {
                        goToHome()
                    }
                } else {
                    val errorCode = response.message
                    preferencesManager.logout()
                    goToHome()

                    runOnUiThread {
                        Toast.makeText(
                            this@SplashScreen,
                            errorCode,
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
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun verifyUserRole(preferencesManager: UserPreferencesManager) {
        if (preferencesManager.isLoggedIn()) {
            check(preferencesManager)
        } else {
            goToHome()
        }
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
