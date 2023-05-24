package com.example.projectmobile.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmobile.LoginActivity
import com.example.projectmobile.MainActivity
import com.example.projectmobile.R
import com.example.projectmobile.util.UserPreferencesManager

class AdminHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        supportActionBar?.hide()

        val preferencesManager = UserPreferencesManager(this)

        val logoutButton: Button = findViewById(R.id.button_logout)
        logoutButton.setOnClickListener {
            preferencesManager.logout()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
