package com.example.projectmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var editNome: EditText
    private lateinit var editEmail: EditText
    private lateinit var editSenha: EditText
    private lateinit var editCPF: EditText
    private lateinit var editDataNascimento: EditText
    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var accountButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        editNome = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editSenha = findViewById(R.id.editPassword)
        editCPF = findViewById(R.id.editCPF)
        editDataNascimento = findViewById(R.id.editBirthday)
        btnRegister = findViewById(R.id.registerButton)
        progressBar = findViewById(R.id.progressBar)
        accountButton = findViewById(R.id.have_account)

        val backButton: ImageButton = findViewById(R.id.returnButton)
        backButton.setOnClickListener {
            finish()
        }

        btnRegister.setOnClickListener {
            register()
        }

        accountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun register() {
        val nome = editNome.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val senha = editSenha.text.toString().trim()
        val cpf = editCPF.text.toString().trim()
        val dataNascimento = editDataNascimento.text.toString().trim()

        if (validarCampos(nome, email, senha, cpf, dataNascimento)) {
            disableFields()
            progressBar.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE
                enableFields()
                clearFields()
            }, 3000)
        }
    }

    private fun validarCampos(
        nome: String,
        email: String,
        senha: String,
        cpf: String,
        dataNascimento: String
    ): Boolean {
        if (TextUtils.isEmpty(nome)) {
            editNome.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(email)) {
            editEmail.error = "Campo obrigatório"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.error = "Email inválido"
            return false
        }

        if (TextUtils.isEmpty(senha)) {
            editSenha.error = "Campo obrigatório"
            return false
        } else if (senha.length < 6) {
            editSenha.error = "A senha deve conter no mínimo 6 caracteres"
            return false
        }

        if (TextUtils.isEmpty(cpf)) {
            editCPF.error = "Campo obrigatório"
            return false
        } else if (cpf.length != 11) {
            editCPF.error = "CPF inválido"
            return false
        }

        if (TextUtils.isEmpty(dataNascimento)) {
            editDataNascimento.error = "Campo obrigatório"
            return false
        } else {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.isLenient = false

            try {
                val date = sdf.parse(dataNascimento)
                val currentDate = Calendar.getInstance().time
                if (date == null || date.after(currentDate)) {
                    editDataNascimento.error = "Data de nascimento inválida"
                    return false
                }
            } catch (e: Exception) {
                editDataNascimento.error = "Data de nascimento inválida"
                return false
            }
        }

        return true
    }

    private fun disableFields() {
        editNome.isEnabled = false
        editEmail.isEnabled = false
        editSenha.isEnabled = false
        editCPF.isEnabled = false
        editDataNascimento.isEnabled = false
        btnRegister.isEnabled = false
    }

    private fun enableFields() {
        editNome.isEnabled = true
        editEmail.isEnabled = true
        editSenha.isEnabled = true
        editCPF.isEnabled = true
        editDataNascimento.isEnabled = true
        btnRegister.isEnabled = true
    }

    private fun clearFields() {
        editNome.setText("")
        editEmail.setText("")
        editSenha.setText("")
        editCPF.setText("")
        editDataNascimento.setText("")
    }
}