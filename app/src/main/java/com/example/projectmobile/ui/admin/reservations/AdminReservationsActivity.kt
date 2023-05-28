package com.example.projectmobile.ui.admin.reservations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class AdminReservationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_reservations)

        supportActionBar?.hide()

        val backButton: ImageButton = findViewById(R.id.returnButton)
        backButton.setOnClickListener {
            finish()
        }

        handleSearch("")
    }

    private fun handleSearch(search: String) {
        val preferencesManager = UserPreferencesManager(this)
        getReservationsData(preferencesManager, search)
    }

    private fun getReservationsData(preferencesManager: UserPreferencesManager, search: String) {
        val apiService = APIService(preferencesManager.getToken())
        val url = "/reservation/list?page=1&size=100&sort=ASC&search=$search"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data = response.reservations
                    println(data)
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@AdminReservationsActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@AdminReservationsActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
