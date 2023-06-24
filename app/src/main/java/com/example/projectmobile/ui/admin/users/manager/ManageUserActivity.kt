package com.example.projectmobile.ui.admin.users.manager

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.User
import com.example.projectmobile.databinding.ActivityCreateUserBinding
import com.example.projectmobile.ui.admin.users.AdminUsersActivity
import com.example.projectmobile.ui.admin.users.reservations.UserReservationsActivity
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ManageUserActivity : AppCompatActivity() {
    private var userId: Int? = null
    private var _binding: ActivityCreateUserBinding? = null
    private val binding get() = _binding!!
    private val userTypes = arrayOf("CLIENT", "ADMIN")

    private lateinit var spinnerUserType: Spinner
    private lateinit var preferencesManager: UserPreferencesManager
    private var userRole = "CLIENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerUserType = binding.spinnerUserType
        preferencesManager = UserPreferencesManager(this)

        supportActionBar?.hide()

        binding.returnButton.setOnClickListener {
            finish()
        }

        verifySelectedItem()

        binding.editButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableFields()

                binding.registerButton.visibility = View.GONE
                binding.saveButton.visibility = View.VISIBLE
                binding.reservationUserButton.visibility = View.GONE
                configureUserTypeSpinner()
            } else {
                disableFields()
                loaded(true)
                binding.saveButton.visibility = View.GONE
                if(binding.userType.text == userRole){
                    binding.reservationUserButton.visibility = View.VISIBLE
                }
            }
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

        binding.saveButton.setOnClickListener {
            val name = binding.editName.text.toString()
            val email = binding.editEmail.text.toString()
            val cpf = binding.editCPF.text.toString()
            val bornAt = binding.editBirthday.text.toString()

            if (validateFields(name, email, null, cpf, bornAt)) {
                saveData()
            }
        }

        binding.reservationUserButton.setOnClickListener {
            if(userId != null){
                preferencesManager.saveTempId(userId.toString())
                startActivity(Intent(this@ManageUserActivity, UserReservationsActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferencesManager.removeSelectedUser()
    }

    private fun verifySelectedItem() {
        val item: User? = preferencesManager.getSelectedUser()

        if (item != null) {
            userId = item.id
            binding.userType.text = item.role
            binding.titleTextView.text = "ID: $userId"
            binding.editName.setText(item.name)
            binding.editEmail.setText(item.email)

            binding.editPassword.visibility = View.GONE

            binding.editCPF.setText(item.cpf)
            binding.editBirthday.setText(dateFormatter(item.bornAt))
            binding.editPhone.setText(item.phone)
            binding.editStreet.setText(item.street)
            binding.editNumber.setText(item.number.toString())
            binding.editNeighborhood.setText(item.neighborhood)
            binding.editState.setText(item.state)
            binding.editCity.setText(item.city)
            binding.editCountry.setText(item.country)

            binding.editButton.visibility = View.VISIBLE
            if(binding.userType.text == userRole){
                binding.reservationUserButton.visibility = View.VISIBLE
            }
            binding.registerButton.visibility = View.GONE

            disableFields()
        } else {
            configureUserTypeSpinner()
        }
    }

    private fun configureUserTypeSpinner() {
        // Definir as opções do Spinner
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, userTypes)

        // Especificar o layout a ser usado quando as opções aparecerem
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        // Anexar o adaptador ao Spinner
        spinnerUserType.adapter = adapter

        // Definir um ouvinte de seleção para o Spinner
        spinnerUserType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                userRole = userTypes[position]
                binding.userType.text = userTypes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
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
                            this@ManageUserActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    finish()
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded(false)
                        Toast.makeText(
                            this@ManageUserActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    loaded(false)
                    Toast.makeText(
                        this@ManageUserActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun saveData() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/user"

        val requestData = getRequestData()

        println(requestData)

        apiService.putData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@ManageUserActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    finish()
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loaded(true)
                        Toast.makeText(
                            this@ManageUserActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    loaded(true)
                    Toast.makeText(
                        this@ManageUserActivity,
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
        var number: String? = binding.editNumber.text.toString()
        val neighborhood = binding.editNeighborhood.text.toString()
        val state = binding.editState.text.toString()
        val city = binding.editCity.text.toString()
        val country = binding.editCountry.text.toString()

        if (TextUtils.isEmpty(number)) {
            number = null
        }

        return "{\"id\": $userId, " +
                "\"name\": \"$name\", " +
                "\"email\": \"$email\", " +
                "\"password\": \"$password\", " +
                "\"cpf\": \"$cpf\", " +
                "\"bornAt\": \"$bornAt\", " +
                "\"phone\": \"$phone\", " +
                "\"street\": \"$street\", " +
                "\"number\": $number, " +
                "\"neighborhood\": \"$neighborhood\", " +
                "\"state\": \"$state\", " +
                "\"city\": \"$city\", " +
                "\"country\": \"$country\", " +
                "\"role\": \"${userRole}\"}"
    }

    private fun validateFields(
        nome: String,
        email: String,
        senha: String?,
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

        if (senha != null) {
            if (TextUtils.isEmpty(senha)) {
                binding.editPassword.error = "Campo obrigatório"
                return false
            } else if (senha.length < 6) {
                binding.editPassword.error = "A senha deve conter no mínimo 6 caracteres"
                return false
            }
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
        binding.nameRole.visibility = View.GONE
        binding.editEmail.visibility = View.GONE
        binding.editPassword.visibility = View.GONE
        binding.editCPF.visibility = View.GONE
        binding.birthdayPhone.visibility = View.GONE
        binding.addressTextView.visibility = View.GONE
        binding.addressLayout.visibility = View.GONE
        binding.addressLayout2.visibility = View.GONE
        binding.addressLayout3.visibility = View.GONE
        binding.registerButton.visibility = View.GONE
        binding.editButton.visibility = View.GONE
        binding.saveButton.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded(save: Boolean) {
        binding.titleTextView.visibility = View.VISIBLE
        binding.nameRole.visibility = View.VISIBLE
        binding.editEmail.visibility = View.VISIBLE
        binding.editCPF.visibility = View.VISIBLE
        binding.birthdayPhone.visibility = View.VISIBLE
        binding.addressTextView.visibility = View.VISIBLE
        binding.addressLayout.visibility = View.VISIBLE
        binding.addressLayout2.visibility = View.VISIBLE
        binding.addressLayout3.visibility = View.VISIBLE

        if (!save) {
            binding.registerButton.visibility = View.VISIBLE
            binding.editPassword.visibility = View.VISIBLE
        } else {
            binding.saveButton.visibility = View.VISIBLE
            binding.editButton.visibility = View.VISIBLE
        }

        binding.progressBar.visibility = View.GONE
    }

    private fun disableFields() {
        binding.editName.isEnabled = false
        binding.editEmail.isEnabled = false
        binding.editPassword.isEnabled = false
        binding.editCPF.isEnabled = false
        binding.editBirthday.isEnabled = false
        binding.editPhone.isEnabled = false
        binding.editStreet.isEnabled = false
        binding.editNumber.isEnabled = false
        binding.editNeighborhood.isEnabled = false
        binding.editState.isEnabled = false
        binding.editCity.isEnabled = false
        binding.editCountry.isEnabled = false
        binding.spinnerUserType.isEnabled = false
    }

    private fun enableFields() {
        binding.editName.isEnabled = true
        binding.editEmail.isEnabled = true
        binding.editPassword.isEnabled = true
        binding.editCPF.isEnabled = true
        binding.editBirthday.isEnabled = true
        binding.editPhone.isEnabled = true
        binding.editStreet.isEnabled = true
        binding.editNumber.isEnabled = true
        binding.editNeighborhood.isEnabled = true
        binding.editState.isEnabled = true
        binding.editCity.isEnabled = true
        binding.editCountry.isEnabled = true
        binding.spinnerUserType.isEnabled = true
    }

    private fun dateFormatter(dataString: String): String? {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return entryFormat.parse(dataString)?.let { exitFormat.format(it) }
    }
}
