package com.example.projectmobile.ui.admin.users

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

class AdminUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_users)

        supportActionBar?.hide()

        val backButton: ImageButton = findViewById(R.id.returnButton)
        backButton.setOnClickListener {
            finish()
        }

        handleSearch("")
    }

    private fun handleSearch(search: String) {
        val preferencesManager = UserPreferencesManager(this)
        getUsersData(preferencesManager, search)
    }

    private fun getUsersData(preferencesManager: UserPreferencesManager, search: String) {
        val apiService = APIService(preferencesManager.getToken())
        val url = "/user/list?page=1&size=100&sort=ASC&search=$search"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data = response.users
                    println(data)
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        Toast.makeText(
                            this@AdminUsersActivity,
                            errorCode,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onError(error: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@AdminUsersActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
