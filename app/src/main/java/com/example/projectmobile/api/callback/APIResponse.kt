package com.example.projectmobile.api.callback

import com.example.projectmobile.api.types.AuthData

data class APIResponse(
    val error: Boolean,
    val data: Any?,
    val message: String,
    val authData: AuthData?
)
