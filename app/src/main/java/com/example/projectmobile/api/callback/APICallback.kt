package com.example.projectmobile.api.callback

import com.example.projectmobile.api.types.APIResponse
import java.io.IOException

interface APICallback {
    fun onSuccess(response: APIResponse)
    fun onError(error: IOException)
}
