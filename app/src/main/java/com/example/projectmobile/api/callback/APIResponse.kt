package com.example.projectmobile.api.callback

data class APIResponse(
    val error: Boolean,
    val data: Any?,
    val message: String,
    val authData: Any?
)
