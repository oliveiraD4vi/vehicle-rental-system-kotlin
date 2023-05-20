package com.example.projectmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import com.example.projectmobile.ui.admin.AdminHomeActivity
import com.example.projectmobile.util.UserPreferencesManager

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 2000 // Tempo em milissegundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            verifyUserRole()
        }, SPLASH_TIME_OUT)
    }

    private fun verifyUserRole() {
        val preferencesManager = UserPreferencesManager(this)

        if (preferencesManager.isLoggedIn() && preferencesManager.getRole() == "ADMIN") {
            val intent = Intent(this, AdminHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
