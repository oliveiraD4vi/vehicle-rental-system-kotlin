package com.example.projectmobile.ui.admin

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmobile.R
import com.example.projectmobile.models.user.UserViewModel

class AdminHomeActivity : AppCompatActivity() {
    val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        supportActionBar?.hide()

//        val text: TextView = findViewById(R.id.text_content)
//        text.text = userViewModel.token
    }
}