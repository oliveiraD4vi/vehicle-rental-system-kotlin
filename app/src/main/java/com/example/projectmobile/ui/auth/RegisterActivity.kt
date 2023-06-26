package com.example.projectmobile.ui.auth

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.ActivityFormReservationVehicleBinding
import com.example.projectmobile.databinding.ActivityRegisterBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var editNome: EditText
    private lateinit var editEmail: EditText
    private lateinit var editSenha: EditText
    private lateinit var editCPF: EditText
    private lateinit var editDataNascimento: Button
    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var accountButton: Button

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

        binding.editBirthday.setOnClickListener {
            handleDate()
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

            sendDataToServer()
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

    private fun sendDataToServer() {
        val apiService = APIService()
        val url = "/user/register"

        val requestData = constructRequestData()

        apiService.postData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    runOnUiThread {
                        Toast.makeText(
                            this@RegisterActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        enableFields()
                        clearFields()
                        Toast.makeText(
                            this@RegisterActivity,
                            errorCode,
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
                        this@RegisterActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun constructRequestData(): String {
        val name = editNome.text.toString()
        val email = editEmail.text.toString()
        val password = editSenha.text.toString()
        val birthday = dateFormatter(editDataNascimento.text.toString())
        val cpf = editCPF.text.toString()

        return "{\"name\": \"$name\", \"email\": \"$email\", \"password\": \"$password\", \"bornAt\": \"$birthday\", \"cpf\": \"$cpf\"}"
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

    private fun dateFormatter(dataString: String): String? {
        val entryFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val exitFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        return entryFormat.parse(dataString)?.let { exitFormat.format(it) } ?: "?"
    }

    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dueDate = dateFormat.format(calendar.time)
        binding.editBirthday.text = dueDate

    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }
}
