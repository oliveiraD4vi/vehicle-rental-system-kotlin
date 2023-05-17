package com.example.projectmobile.ui.admin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmobile.R
import com.example.projectmobile.util.UserPreferencesManager

class AdminHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        supportActionBar?.hide()

        val preferencesManager = UserPreferencesManager(this)
        val role = preferencesManager.getRole()

        val text: TextView = findViewById(R.id.text_content)
        text.text = role
    }
}
