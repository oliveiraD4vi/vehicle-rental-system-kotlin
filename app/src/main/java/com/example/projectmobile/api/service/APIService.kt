package com.example.projectmobile.api.service

import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.types.APIResponse
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class APIService {
    private val client = OkHttpClient()
    private val json = "application/json; charset=utf-8".toMediaTypeOrNull()
    private val baseurl = "http://18.231.159.161:8080/api"

    fun getData(url: String, callback: APICallback) {
        val request = Request.Builder()
            .url(baseurl + url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val apiResponse = Gson().fromJson(responseData, APIResponse::class.java)

                    callback.onSuccess(apiResponse)
                } else {
                    callback.onError(IOException("Erro na chamada da API: " + response.code))
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    fun postData(url: String, requestData: String, callback: APICallback) {
        val requestBody = requestData.toRequestBody(json)

        val request = Request.Builder()
            .url(baseurl + url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val apiResponse = Gson().fromJson(responseData, APIResponse::class.java)

                    callback.onSuccess(apiResponse)
                } else {
                    callback.onError(IOException("Erro na chamada da API: " + response.code))
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }
}

