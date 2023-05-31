package com.example.projectmobile.ui.admin.users

import android.app.Dialog
import android.content.Intent
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
import com.example.projectmobile.api.types.User
import com.example.projectmobile.databinding.ActivityAdminCarsBinding
import com.example.projectmobile.databinding.ActivityAdminUsersBinding
import com.example.projectmobile.databinding.ModalLayoutBinding
import com.example.projectmobile.ui.admin.users.adapter.AdminUserAdapter
import com.example.projectmobile.ui.admin.users.user.VisualizeUserActivity
import com.example.projectmobile.util.UserPreferencesManager
import java.io.IOException

class AdminUsersActivity : AppCompatActivity(), UserClickListener {
    private var _binding: ActivityAdminUsersBinding? = null
    private val binding get() = _binding!!
    private val adapter = AdminUserAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //adapter
        binding.recyclerUsers.adapter = adapter

        //layout
        binding.recyclerUsers.layoutManager = LinearLayoutManager(this)

        binding.returnButton.setOnClickListener {
            finish()
        }

        handleSearch()

        binding.imageSearch.setOnClickListener {
            handleSearch()
        }
    }

    private fun handleSearch() {
        val search = binding.editResearch.text.toString()
        val preferencesManager = UserPreferencesManager(this)
        getUsersData(preferencesManager, search)
    }

    private fun getUsersData(preferencesManager: UserPreferencesManager, search: String) {
        loading()
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
                        loaded()
                    }
                } else {
                    val errorCode = response.message

                    runOnUiThread {
                        loadedWithZero()
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
                    loadedWithZero()
                    Toast.makeText(
                        this@AdminUsersActivity,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun loading() {
        binding.progressBar.visibility = View.VISIBLE

        binding.notFound.visibility = View.GONE
        binding.recyclerUsers.visibility = View.GONE
    }

    private fun loaded() {
        binding.progressBar.visibility = View.GONE

        binding.notFound.visibility = View.GONE
        binding.recyclerUsers.visibility = View.VISIBLE
    }

    private fun loadedWithZero() {
        binding.progressBar.visibility = View.GONE

        binding.notFound.visibility = View.VISIBLE
        binding.recyclerUsers.visibility = View.GONE
    }

    override fun onEditCarClick(user: User) {
        startActivity(Intent(this@AdminUsersActivity, VisualizeUserActivity::class.java))
    }

    override fun onDeleteCarClick(user: User) {
        showDeleteConfirmationDialog(user)
    }

    private fun showDeleteConfirmationDialog(user: User) {
        val dialog = Dialog(this)
        val dialogBinding: ModalLayoutBinding = ModalLayoutBinding.inflate(layoutInflater)
        val dialogView = dialogBinding.root
        dialog.setContentView(dialogView)

        dialogBinding.btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnConfirmar.setOnClickListener {
            deleteItem(user)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteItem(user: User) {
        Toast.makeText(this, "${user.name} deletado", Toast.LENGTH_SHORT).show()
    }
}
