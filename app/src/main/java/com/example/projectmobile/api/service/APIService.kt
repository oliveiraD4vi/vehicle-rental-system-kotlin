package com.example.projectmobile.api.service

import com.example.projectmobile.api.callback.APICallback
import com.example.projectmobile.api.types.APIResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class APIService(private val token: String? = null) {
    private val client = OkHttpClient()
    private val json = "application/json; charset=utf-8".toMediaTypeOrNull()
    private val baseurl = "http://18.231.159.161:8080/api"

    fun getData(url: String, callback: APICallback) {
        val requestBuilder = Request.Builder()
            .url(baseurl + url)

        token?.let {
            requestBuilder.addHeader("Authorization", "BearerToken $it")
        }

        val request = requestBuilder.build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
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

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    fun postData(url: String, requestData: String, callback: APICallback) {
        val requestBuilder = Request.Builder()
            .url(baseurl + url)
            .post(requestData.toRequestBody(json))

        token?.let {
            requestBuilder.addHeader("Authorization", "BearerToken $it")
        }

        val request = requestBuilder.build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
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

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    fun putData(url: String, requestData: String, callback: APICallback) {
        val requestBuilder = Request.Builder()
            .url(baseurl + url)
            .put(requestData.toRequestBody(json))

        token?.let {
            requestBuilder.addHeader("Authorization", "BearerToken $it")
        }

        val request = requestBuilder.build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
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

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    fun deleteData(url: String, callback: APICallback) {
        val requestBuilder = Request.Builder()
            .url(baseurl + url)
            .delete()

        token?.let {
            requestBuilder.addHeader("Authorization", "BearerToken $it")
        }

        val request = requestBuilder.build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
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

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }
}
