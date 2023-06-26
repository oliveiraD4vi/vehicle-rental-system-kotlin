package com.example.projectmobile.ui.admin.reservations.manager

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.projectmobile.MainActivity
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.*
import com.example.projectmobile.databinding.ActivityCreateReservationBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ManageReservationActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private var id: String = ""
    private var reservationId: Int? = null
    private var _binding: ActivityCreateReservationBinding? = null
    private var currentStatus: String? = null
    private var currentStep: String? = null

    private val binding get() = _binding!!

    private val statusList = arrayOf("CREATED", "CONFIRMED", "PICKUP", "FINALIZED")
    private val stepList = arrayOf("PERSONAL", "VEHICLE", "PAYMENT", "CONCLUDED")

    private lateinit var spinnerStatus: Spinner
    private lateinit var spinnerStep: Spinner
    private lateinit var preferencesManager: UserPreferencesManager

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerStatus = binding.spinnerStatus
        spinnerStep = binding.spinnerStep
        preferencesManager = UserPreferencesManager(this)

        supportActionBar?.hide()

        binding.returnButton.setOnClickListener {
            finish()
        }

        binding.pickup.setOnClickListener {
            id = binding.pickup.id.toString()
            handleDate()
        }

        binding.devolution.setOnClickListener {
            id = binding.devolution.id.toString()
            handleDate()
        }

        configureSpinner()

        verifySelectedItem()

        binding.editButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableFields()

                binding.registerButton.visibility = View.GONE
                binding.idLayout.visibility = View.GONE
                binding.pickup.visibility = View.GONE
                binding.devolution.visibility = View.GONE
                binding.idStepStatusText.visibility = View.GONE

                binding.idLayoutTwo.visibility = View.GONE
                binding.textUserId.visibility = View.GONE
                binding.editUserName.visibility = View.GONE
                binding.editUserCpf.visibility = View.GONE
                binding.editInfoEmail.visibility = View.GONE

                binding.idLayoutCar.visibility = View.GONE
                binding.textCarId.visibility = View.GONE
                binding.editCarName.visibility = View.GONE
                binding.editCarPlate.visibility = View.GONE
                binding.editCarDaily.visibility = View.GONE

                binding.idStepStatus.visibility = View.VISIBLE
                binding.saveButton.visibility = View.VISIBLE

            } else {
                disableFields()

                binding.idLayout.visibility = View.VISIBLE
                binding.pickup.visibility = View.VISIBLE
                binding.devolution.visibility = View.VISIBLE
                binding.idStepStatusText.visibility = View.VISIBLE

                binding.idLayoutTwo.visibility = View.VISIBLE
                binding.textUserId.visibility = View.VISIBLE
                binding.editUserName.visibility = View.VISIBLE
                binding.editUserCpf.visibility = View.VISIBLE
                binding.editInfoEmail.visibility = View.VISIBLE

                binding.idLayoutCar.visibility = View.VISIBLE
                binding.textCarId.visibility = View.VISIBLE
                binding.editCarName.visibility = View.VISIBLE
                binding.editCarPlate.visibility = View.VISIBLE
                binding.editCarDaily.visibility = View.VISIBLE

                binding.idStepStatus.visibility = View.GONE
                binding.saveButton.visibility = View.GONE

            }
        }

        binding.registerButton.setOnClickListener {
            val userId = binding.userId.text.toString()
            val vehicleId = binding.vehicleId.text.toString()
            val pickup = binding.pickup.text.toString()
            val devolution = binding.devolution.text.toString()

            if (validateFields(userId, vehicleId, pickup, devolution)) {
                sendDataToServer()
            }
        }

        binding.saveButton.setOnClickListener {
            if (currentStatus != null && currentStep != null) {
                saveData()
            }
        }
        verifyRuleLayout()

    }

    override fun onDestroy() {
        super.onDestroy()
        preferencesManager.removeSelectedReservation()
    }

    private fun verifyRuleLayout() {
        if (binding.editButton.visibility == View.VISIBLE) {
            binding.idLayoutTwo.visibility = View.VISIBLE
            binding.textUserId.visibility = View.VISIBLE
            binding.editUserName.visibility = View.VISIBLE
            binding.editUserCpf.visibility = View.VISIBLE
            binding.editInfoEmail.visibility = View.VISIBLE

            binding.idLayoutCar.visibility = View.VISIBLE
            binding.textCarId.visibility = View.VISIBLE
            binding.editCarName.visibility = View.VISIBLE
            binding.editCarPlate.visibility = View.VISIBLE
            binding.editCarDaily.visibility = View.VISIBLE
        }
    }

    private fun getUser() {

        val item: Reservation? = preferencesManager.getSelectedReservation()

        val apiService = APIService(preferencesManager.getToken())
        val userId = item?.user_id
        val url = "/user/personal?id=$userId"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data: User? = response.user
                    if (data != null) {
                        showUser(data)
                    }
                } else {
                    val errorCode = response.message.toString()

                    runOnUiThread {
                        Toast.makeText(
                            this@ManageReservationActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {

                runOnUiThread {
                    Toast.makeText(
                        this@ManageReservationActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun showUser(data: User) {
        runOnUiThread {
            binding.textUserId.text = "ID: ${data.id.toString()}"
            binding.editUserName.setText(data.name.toString())
            binding.editUserCpf.setText(data.cpf.toString())
            binding.editInfoEmail.setText(data.email.toString())
        }
    }

    private fun getCar() {
        val item: Reservation? = preferencesManager.getSelectedReservation()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/vehicle?id=${item?.vehicle_id}"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data = response.vehicle
                    if (data != null) {
                        runOnUiThread {
                            showCar(data)
                        }

                    }

                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        finish()
                        Toast.makeText(
                            this@ManageReservationActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    finish()
                    Toast.makeText(
                        this@ManageReservationActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun showCar(data: Car) {
        runOnUiThread {
            binding.textCarId.text = "ID: " + data.id.toString()
            binding.editCarName.setText(data.brand.toString() + " " + data.model.toString())
            binding.editCarPlate.setText(data.plate.toString())
            binding.editCarDaily.setText("R$ " + data.value.toString())
        }
    }

    private fun verifySelectedItem() {
        val item: Reservation? = preferencesManager.getSelectedReservation()

        if (item != null) {
            reservationId = item.id
            binding.titleTextView.text = "ID: $reservationId"
            binding.userId.setText(item.user_id.toString())
            binding.vehicleId.setText(item.vehicle_id.toString())
            binding.pickup.setText(dateFormatter(item.pickup))
            binding.devolution.setText(dateFormatter(item.devolution))

            currentStatus = item.status.toString()
            binding.status.text = item.status.toString()
            binding.layoutStatus.text = item.status.toString()

            currentStep = item.step.toString()
            binding.step.text = item.step.toString()
            binding.layoutStep.text = item.step.toString()

            binding.idStepStatusText.visibility = View.VISIBLE
            binding.editButton.visibility = View.VISIBLE
            binding.registerButton.visibility = View.GONE

            disableFields()
            getUser()
            getCar()
        }
    }

    private fun configureSpinner() {
        // Definir as opções do Spinner
        val statusAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, statusList)
        val stepAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, stepList)

        // Especificar o layout a ser usado quando as opções aparecerem
        statusAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        stepAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        // Anexar o adaptador ao Spinner
        spinnerStatus.adapter = statusAdapter
        spinnerStep.adapter = stepAdapter

        // Definir um ouvinte de seleção para o Spinner
        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                currentStatus = statusList[position]
                binding.status.text = statusList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerStep.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                currentStep = stepList[position]
                binding.step.text = stepList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun validateFields(
        userId: String,
        vehicleId: String,
        pickup: String,
        devolution: String,
    ): Boolean {
        if (TextUtils.isEmpty(userId)) {
            binding.userId.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(vehicleId)) {
            binding.vehicleId.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(pickup)) {
            binding.pickup.error = "Campo obrigatório"
            return false
        }

        if (TextUtils.isEmpty(devolution)) {
            binding.devolution.error = "Campo obrigatório"
            return false
        }

        return true
    }

    private fun sendDataToServer() {
        loading()
        val apiService = APIService(preferencesManager.getToken())
        val url = "/reservation"

        val requestData = getRequestData()

        apiService.postData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@ManageReservationActivity,
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
                            this@ManageReservationActivity,
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
                        this@ManageReservationActivity,
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
        val url = "/reservation"

        val requestData = getRequestSaveData()

        apiService.putData(url, requestData, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val message = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@ManageReservationActivity,
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
                            this@ManageReservationActivity,
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
                        this@ManageReservationActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun getRequestData(): String {
        val userId = binding.userId.text.toString()
        val vehicleId = binding.vehicleId.text.toString()
        val pickup = binding.pickup.text.toString()
        val devolution = binding.devolution.text.toString()

        return "{\"user_id\": \"$userId\", " +
                "\"vehicle_id\": \"$vehicleId\", " +
                "\"pickup\": \"${dateFormatterTwo(pickup)}\", " +
                "\"devolution\": \"${dateFormatterTwo(devolution)}\"}"
    }

    private fun getRequestSaveData(): String {
        return "{\"id\": $reservationId, " +
                "\"status\": \"$currentStatus\", " +
                "\"step\": \"$currentStep\"}"
    }

    private fun loading() {
        binding.titleTextView.visibility = View.GONE
        binding.idLayout.visibility = View.GONE
        binding.idStepStatus.visibility = View.GONE
        binding.idStepStatusText.visibility = View.GONE
        binding.pickup.visibility = View.GONE
        binding.devolution.visibility = View.GONE
        binding.registerButton.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loaded(save: Boolean) {
        binding.titleTextView.visibility = View.VISIBLE
        binding.idLayout.visibility = View.VISIBLE
        binding.pickup.visibility = View.VISIBLE
        binding.devolution.visibility = View.VISIBLE

        if (!save) {
            binding.registerButton.visibility = View.VISIBLE
        } else {
            binding.idStepStatusText.visibility = View.VISIBLE
        }

        binding.progressBar.visibility = View.GONE
    }

    private fun disableFields() {
        binding.editInfoEmail.isEnabled = false
        binding.editUserCpf.isEnabled = false
        binding.editUserName.isEnabled = false
        binding.textCarId.isEnabled = false
        binding.editCarName.isEnabled = false
        binding.editCarPlate.isEnabled = false
        binding.editCarDaily.isEnabled = false
        binding.userId.isEnabled = false
        binding.vehicleId.isEnabled = false
        binding.pickup.isEnabled = false
        binding.devolution.isEnabled = false
    }

    private fun enableFields() {
        binding.userId.isEnabled = true
        binding.vehicleId.isEnabled = true
        binding.pickup.isEnabled = true
        binding.devolution.isEnabled = true
    }

    private fun dateFormatter(dataString: String): String? {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return entryFormat.parse(dataString)?.let { exitFormat.format(it) } ?: "?"
    }

    private fun dateFormatterTwo(dataString: String): String? {
        val entryFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val exitFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        return entryFormat.parse(dataString)?.let { exitFormat.format(it) } ?: "?"
    }

    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dueDate = dateFormat.format(calendar.time)
        if (id == com.example.projectmobile.R.id.pickup.toString()) {
            binding.pickup.text = dueDate
        } else if (id == com.example.projectmobile.R.id.devolution.toString()) {
            binding.devolution.text = dueDate
        }
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }
}
