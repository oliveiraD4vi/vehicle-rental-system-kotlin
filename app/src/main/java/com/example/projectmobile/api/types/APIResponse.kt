package com.example.projectmobile.api.types

import com.example.projectmobile.api.types.AuthData
import com.example.projectmobile.model.Cars

data class APIResponse(
    val error: Boolean,
    val data: Any?,
    val vehicles: ArrayList<Cars>?,
    val message: String,
    val authData: AuthData?,
    val user: UserData?
)
