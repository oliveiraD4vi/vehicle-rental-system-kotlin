package com.example.projectmobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.ui.admin.AdminHomeActivity
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    private val apiService = APIService()

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

    private fun clearFields() {
        editTextEmail.setText("")
        editTextPassword.setText("")
    }

    private fun sendDataToServer() {
        val url = "/user/login"
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        val requestData = "{\"email\": \"$email\", \"password\": \"$password\"}"

        apiService.postData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data = response.authData

                    val preferencesManager = UserPreferencesManager(this@LoginActivity)
                    preferencesManager.login()
                    preferencesManager.saveUserId(data?.userId.toString())
                    preferencesManager.saveToken(data?.token.toString())
                    preferencesManager.saveRole(data?.role.toString())

                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        enableFields()
                        clearFields()
                    }

                    if (data?.role == "CLIENT") {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@LoginActivity, AdminHomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                } else {
                    // A resposta da API indica um erro
                    val errorCode = response.message.toString() // Obtém o código de erro da API

                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        enableFields()
                        clearFields()
                        Toast.makeText(
                            this@LoginActivity,
                            "Erro na chamada da API. Código: $errorCode",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    enableFields()
                    clearFields()
                    Toast.makeText(
                        this@LoginActivity,
                        "Erro na chamada da API. Código: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
