package com.example.projectmobile.ui.formreservation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.example.projectmobile.MainActivity
import com.example.projectmobile.R
import com.example.projectmobile.databinding.ActivityFormReservationVehicleBinding
import com.example.projectmobile.util.UserPreferencesManager
import java.text.SimpleDateFormat
import java.util.*

class FormReservationVehicleActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {
    private var id: String = ""

    private lateinit var binding: ActivityFormReservationVehicleBinding

    @SuppressLint("SimpleDateFormat")
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormReservationVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val preferencesManager = UserPreferencesManager(this)
        // Verify selected vehicle
        verifySelectedCar(preferencesManager)
        // Verify if a date is already selected
        verifySelectedDate(preferencesManager)

        binding.buttonWithdrawalVehicleForm.setOnClickListener(this)
        binding.buttonDeliveryVehicleForm.setOnClickListener(this)
        binding.buttonCancelVehicleForm.setOnClickListener(this)
        binding.returnButton.setOnClickListener(this)
        binding.buttonNextVehicleForm.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_withdrawal_vehicle_form -> {
                id = view.id.toString()
                handleDate()
            }
            R.id.button_delivery_vehicle_form -> {
                id = view.id.toString()
                handleDate()
            }
            R.id.button_cancel_vehicle_form -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.returnButton -> {
                finish()
            }
            R.id.button_next_vehicle_form -> {
                val dataWithdrawal: String = binding.buttonWithdrawalVehicleForm.text.toString()
                val dataDelivery: String = binding.buttonDeliveryVehicleForm.text.toString()
                handleContinue(dataWithdrawal, dataDelivery)
            }
        }
    }

    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val preferencesManager = UserPreferencesManager(this)

        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dueDate = dateFormat.format(calendar.time)
        if (id == R.id.button_withdrawal_vehicle_form.toString()) {
            preferencesManager.saveWithdrawDate(dueDate)
            binding.buttonWithdrawalVehicleForm.text = dueDate
        } else if (id == R.id.button_delivery_vehicle_form.toString()) {
            preferencesManager.saveDeliveryDate(dueDate)
            binding.buttonDeliveryVehicleForm.text = dueDate
        }
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun handleContinue(dataWithdrawal: String, dataDelivery: String) {
        if (dataWithdrawal == "ESCOLHA UMA DATA" || dataDelivery == "ESCOLHA UMA DATA") {
            Toast.makeText(
                applicationContext,
                "As datas devem ser definidas!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            startActivity(Intent(this, FormReservationPaymentActivity::class.java))
        }
    }

    private fun verifySelectedCar(preferencesManager: UserPreferencesManager) {
        val car = preferencesManager.getSelectedCar()

        if (car != null) {
            binding.textNameCar.text = "${car.brand} ${car.model}"
            binding.textPriceCar.text = "R$ ${car.value}"
        } else {
            binding.buttonNextVehicleForm.isEnabled = false

            Toast.makeText(
                applicationContext,
                "O carro deve ser selecionado!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun verifySelectedDate(preferencesManager: UserPreferencesManager) {
        val wDate = preferencesManager.getWithdrawDate()
        if (wDate != null) {
            binding.buttonWithdrawalVehicleForm.text = wDate
        }

        val dDate = preferencesManager.getDeliveryDate()
        if (dDate != null) {
            binding.buttonDeliveryVehicleForm.text = dDate
        }
    }
}
