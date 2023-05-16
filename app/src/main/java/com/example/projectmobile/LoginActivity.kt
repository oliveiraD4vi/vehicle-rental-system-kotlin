package com.example.projectmobile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var noAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        editTextEmail = findViewById(R.id.emailEditText)
        editTextPassword = findViewById(R.id.passwordEditText)
        buttonSubmit = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressBar)
        noAccount = findViewById(R.id.no_account)

        val backButton: ImageButton = findViewById(R.id.returnButton)
        backButton.setOnClickListener {
            finish()
        }

        buttonSubmit.setOnClickListener {
            if (validateFields()) {
                disableFields()
                progressBar.visibility = View.VISIBLE
                sendDataToServer()
            }
        }

        noAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun validateFields(): Boolean {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (email.isEmpty()) {
            editTextEmail.error = "Campo obrigatório"
            return false
        }

        if (!isValidEmail(email)) {
            editTextEmail.error = "Email inválido"
            return false
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Campo obrigatório"
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun disableFields() {
        editTextEmail.isEnabled = false
        editTextPassword.isEnabled = false
        buttonSubmit.isEnabled = false
    }

    private fun enableFields() {
        editTextEmail.isEnabled = true
        editTextPassword.isEnabled = true
        buttonSubmit.isEnabled = true
    }

    private fun sendDataToServer() {
        Handler(Looper.getMainLooper()).postDelayed({
            progressBar.visibility = View.GONE
            enableFields()
        }, 3000)
    }
}
