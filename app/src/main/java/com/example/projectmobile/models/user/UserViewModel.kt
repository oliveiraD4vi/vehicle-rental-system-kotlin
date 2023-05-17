package com.example.projectmobile.models.user

import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    var userId: String? = null
    var token: String? = null
    var role: String? = null
}
