package com.example.projectmobile.util

import android.content.Context
import android.content.SharedPreferences

class UserPreferencesManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("USER_CREDENTIALS", Context.MODE_PRIVATE)

    companion object {
        const val USER_ID = "userId"
        const val TOKEN = "token"
        const val ROLE = "role"
        const val LOGGED_IN = "loggedIn";
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

    fun login() {
        sharedPreferences.edit().putString(LOGGED_IN, "true").apply()
    }

    fun isLoggedIn(): Boolean {
        if (sharedPreferences.getString(LOGGED_IN, null) == "true") {
            return true
        }

        return false
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}
