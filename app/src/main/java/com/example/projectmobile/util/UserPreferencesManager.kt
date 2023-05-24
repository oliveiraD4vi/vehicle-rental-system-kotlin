package com.example.projectmobile.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projectmobile.api.types.Cars
import com.example.projectmobile.api.types.UserData
import com.google.gson.Gson

class UserPreferencesManager(private val context: Context) {
    private val gson = Gson()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("USER_CREDENTIALS", Context.MODE_PRIVATE)

    companion object {
        const val USER_ID = "userId"
        const val TOKEN = "token"
        const val ROLE = "role"
        const val LOGGED_IN = "loggedIn"
        const val DATA = "userData"
        const val SELECTED_CAR = "selectedCar"
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

    fun saveData(data: UserData) {
        val json = gson.toJson(data)

        sharedPreferences.edit().putString(DATA, json).apply()
    }

    fun getUserData(): UserData? {
        val data = sharedPreferences.getString(DATA, null)

        if (data != null) {
            return gson.fromJson(data, UserData::class.java)
        }

        return null
    }

    fun saveSelectedCar(car: Cars) {
        val json = gson.toJson(car)

        sharedPreferences.edit().putString(SELECTED_CAR, json).apply()
    }

    fun getSelectedCar(): Cars? {
        val data = sharedPreferences.getString(SELECTED_CAR, null)

        if (data != null) {
            return gson.fromJson(data, Cars::class.java)
        }

        return null
    }
}
