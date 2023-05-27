package com.example.projectmobile.ui.formreservation.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projectmobile.MainActivity
import com.example.projectmobile.R
import com.example.projectmobile.databinding.ActivityFormReservationPaymentBinding
import com.example.projectmobile.ui.formreservation.map.MapViewActivity

class FormReservationPaymentActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFormReservationPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormReservationPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.returnButton.setOnClickListener(this)
        binding.buttonCancelPaymentForm.setOnClickListener(this)
        binding.buttonConfirmPaymentForm.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.returnButton) {
            finish()
        } else if (view.id == R.id.button_cancel_payment_form) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if (view.id == R.id.button_confirm_payment_form) {
            startActivity(Intent(this, MapViewActivity::class.java))
            finish()
        }
    }
}
