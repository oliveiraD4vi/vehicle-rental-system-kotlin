package com.example.projectmobile.api.callback

import java.io.IOException

interface APICallback {
    fun onSuccess(response: APIResponse)
    fun onError(error: IOException)
}
