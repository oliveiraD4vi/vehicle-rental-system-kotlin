package com.example.projectmobile.api.service

import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.types.APIResponse
import com.example.projectmobile.api.types.HTTPMethods
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class APIService(private val token: String? = null) {
    private val client = OkHttpClient()
    private val json = "application/json; charset=utf-8".toMediaTypeOrNull()
    private val baseurl = "http://15.228.52.63:8080/api"

    private fun buildRequest(
        url: String,
        method: HTTPMethods,
        requestData: String? = null
    ): Request {
        val requestBuilder = Request.Builder().url(baseurl + url)

        token?.let {
            requestBuilder.addHeader("Authorization", "BearerToken $it")
        }

        when (method) {
            HTTPMethods.GET -> requestBuilder.get()
            HTTPMethods.POST -> {
                val requestBody = requestData?.toRequestBody(json)
                if (requestBody != null) {
                    requestBuilder.post(requestBody)
                }
            }
            HTTPMethods.PUT -> {
                val requestBody = requestData?.toRequestBody(json)
                if (requestBody != null) {
                    requestBuilder.put(requestBody)
                }
            }
            HTTPMethods.DELETE -> requestBuilder.delete()
        }

        return requestBuilder.build()
    }

    private fun handleResponse(response: Response, callback: APICallback) {
        val responseData = response.body?.string()
        val apiResponse: APIResponse? = if (response.isSuccessful) {
            Gson().fromJson(responseData, APIResponse::class.java)
        } else {
            try {
                Gson().fromJson(responseData, APIResponse::class.java)
            } catch (e: JsonSyntaxException) {
                null
            }
        }

        if (response.isSuccessful) {
            if (apiResponse != null) {
                callback.onSuccess(apiResponse)
            }
        } else {
            val errorMessage = apiResponse?.message ?: "Erro desconhecido na chamada da API"
            callback.onError(IOException(errorMessage))
        }
    }

    private fun enqueueCall(request: Request, callback: APICallback) {
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                handleResponse(response, callback)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    fun getData(url: String, callback: APICallback) {
        val request = buildRequest(url, HTTPMethods.GET)
        enqueueCall(request, callback)
    }

    fun postData(url: String, requestData: String, callback: APICallback) {
        val request = buildRequest(url, HTTPMethods.POST, requestData)
        enqueueCall(request, callback)
    }

    fun putData(url: String, requestData: String, callback: APICallback) {
        val request = buildRequest(url, HTTPMethods.PUT, requestData)
        enqueueCall(request, callback)
    }

    fun deleteData(url: String, callback: APICallback) {
        val request = buildRequest(url, HTTPMethods.DELETE)
        enqueueCall(request, callback)
    }
}
