package com.example.projectmobile.ui.admin.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmobile.R
import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.service.APIService
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.databinding.ActivityAdminCarsBinding
import com.example.projectmobile.databinding.ActivityAdminUsersBinding
import com.example.projectmobile.ui.admin.users.adapter.AdminUserAdapter
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class AdminUsersActivity : AppCompatActivity() {
    private var _binding: ActivityAdminUsersBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val adapter = AdminUserAdapter()

        //adapter
        binding.recyclerUsers.adapter = adapter

        //layout
        binding.recyclerUsers.layoutManager = LinearLayoutManager(this)

        binding.returnButton.setOnClickListener {
            finish()
        }

        handleSearch("", adapter)
    }

    private fun handleSearch(search: String, adapter: AdminUserAdapter) {
        binding.recyclerUsers.visibility = View.GONE
        val preferencesManager = UserPreferencesManager(this)
        getUsersData(preferencesManager, search, adapter)
    }

    private fun getUsersData(preferencesManager: UserPreferencesManager, search: String, adapter: AdminUserAdapter) {
        val apiService = APIService(preferencesManager.getToken())
        val url = "/user/list?page=1&size=100&sort=ASC&search=$search"

        apiService.getData(url, object : APICallback {
            override fun onSuccess(response: APIResponse) {
                if (!response.error) {
                    val data = response.users
                    if (data != null) {
                        runOnUiThread {
                            adapter.updateUsers(data)
                        }
                    }

                    runOnUiThread {
                        binding.recyclerUsers.visibility = View.VISIBLE
                    }
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
