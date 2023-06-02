package com.example.projectmobile.ui.admin.users.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.ActivityCreateUserBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreateUserActivity : AppCompatActivity() {
    private var _binding: ActivityCreateUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = UserPreferencesManager(this)

        supportActionBar?.hide()

        binding.returnButton.setOnClickListener {
            finish()
        }

        binding.registerButton.setOnClickListener {
            val name = binding.editName.text.toString()
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            val cpf = binding.editCPF.text.toString()
            val bornAt = binding.editBirthday.text.toString()

            if (validateFields(name, email, password, cpf, bornAt)) {
                sendDataToServer()
            }
        }
    }

    private fun sendDataToServer() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/user/register"

        val requestData = getRequestData()

        apiService.postData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@CreateUserActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    finish()
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded()
                        Toast.makeText(
                            this@CreateUserActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    loaded()
                    Toast.makeText(
                        this@CreateUserActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun getRequestData(): String {
        val name = binding.editName.text.toString()
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        val cpf = binding.editCPF.text.toString()
        val bornAt = binding.editBirthday.text.toString()
        val phone = binding.editPhone.text.toString()
        val street = binding.editStreet.text.toString()
        val number = binding.editNumber.text.toString()
        val neighborhood = binding.editNeighborhood.text.toString()
        val state = binding.editState.text.toString()
        val city = binding.editCity.text.toString()
        val country = binding.editCountry.text.toString()

        return """{
            "name": "$name",
            "email": "$email",
            "password": "$password",
            "cpf": "$cpf",
            "bornAt": "$bornAt",
            "phone": "$phone",
            "street": "$street",
            "number": "$number",
            "neighborhood": "$neighborhood",
            "state": "$state",
            "city": "$city",
            "country": "$country"
        }""".trimIndent()
    }

    private fun validateFields(
        nome: String,
        email: String,
        senha: String,
        cpf: String,
        dataNascimento: String
    ): Boolean {
        if (TextUtils.isEmpty(nome)) {
            binding.editName.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(email)) {
            binding.editEmail.error = "Campo obrigatório"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editEmail.error = "Email inválido"
            return false
        }

        if (TextUtils.isEmpty(senha)) {
            binding.editPassword.error = "Campo obrigatório"
            return false
        } else if (senha.length < 6) {
            binding.editPassword.error = "A senha deve conter no mínimo 6 caracteres"
            return false
        }

        if (TextUtils.isEmpty(cpf)) {
            binding.editCPF.error = "Campo obrigatório"
            return false
        } else if (cpf.length != 11) {
            binding.editCPF.error = "CPF inválido"
            return false
        }

        if (TextUtils.isEmpty(dataNascimento)) {
            binding.editBirthday.error = "Campo obrigatório"
            return false
        } else {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.isLenient = false

            try {
                val date = sdf.parse(dataNascimento)
                val currentDate = Calendar.getInstance().time
                if (date == null || date.after(currentDate)) {
                    binding.editBirthday.error = "Data de nascimento inválida"
                    return false
                }
            } catch (e: Exception) {
                binding.editBirthday.error = "Data de nascimento inválida"
                return false
            }
        }

        return true
    }

    private fun loading() {
        binding.titleTextView.visibility = View.GONE
        binding.editName.visibility = View.GONE
        binding.editEmail.visibility = View.GONE
        binding.editPassword.visibility = View.GONE
        binding.editCPF.visibility = View.GONE
        binding.birthdayPhone.visibility = View.GONE
        binding.addressTextView.visibility = View.GONE
        binding.addressLayout.visibility = View.GONE
        binding.addressLayout2.visibility = View.GONE
        binding.addressLayout3.visibility = View.GONE
        binding.registerButton.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded() {
        binding.titleTextView.visibility = View.VISIBLE
        binding.editName.visibility = View.VISIBLE
        binding.editEmail.visibility = View.VISIBLE
        binding.editPassword.visibility = View.VISIBLE
        binding.editCPF.visibility = View.VISIBLE
        binding.birthdayPhone.visibility = View.VISIBLE
        binding.addressTextView.visibility = View.VISIBLE
        binding.addressLayout.visibility = View.VISIBLE
        binding.addressLayout2.visibility = View.VISIBLE
        binding.addressLayout3.visibility = View.VISIBLE
        binding.registerButton.visibility = View.VISIBLE

        binding.progressBar.visibility = View.GONE
    }
}