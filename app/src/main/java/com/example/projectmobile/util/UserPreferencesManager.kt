package com.example.projectmobile.util

import android.content.Context
import android.content.SharedPreferences

class UserPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    companion object {
        const val USER_ID = "userId"
        const val TOKEN = "token"
        const val ROLE = "role"
    }

    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID, null)
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, null)
    }

    fun saveRole(role: String) {
        sharedPreferences.edit().putString(ROLE, role).apply()
    }

    fun getRole(): String? {
        return sharedPreferences.getString(ROLE, null)
    }
}
