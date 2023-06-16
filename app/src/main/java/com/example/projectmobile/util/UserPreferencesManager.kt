package com.example.projectmobile.util

import android.content.Context
import android.content.SharedPreferences
import com.example.projectmobile.api.types.User
import com.example.projectmobile.api.types.Car
import com.example.projectmobile.api.types.Reservation
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
        const val SELECTED_RESERVATION = "selectedReservation"
        const val SELECTED_USER = "selectedUser"
        const val SELECTED_DATE_W = "selectedWithdraw"
        const val SELECTED_DATE_D = "selectedDelivery"
        const val CURRENT_RESERVATION = "currentReservation"
        const val CURRENT_CAR = "currentCar"
    }

    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID, null)
    }

    fun saveReservationId(reservationId: String) {
        sharedPreferences.edit().putString(CURRENT_RESERVATION, reservationId).apply()
    }

    fun getReservationId(): String? {
        return sharedPreferences.getString(CURRENT_RESERVATION, null)
    }

    fun saveVehicleId(reservationId: String) {
        sharedPreferences.edit().putString(CURRENT_CAR, reservationId).apply()
    }

    fun getVehicleId(): String? {
        return sharedPreferences.getString(CURRENT_CAR, null)
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

    fun saveData(data: User) {
        val json = gson.toJson(data)

        sharedPreferences.edit().putString(DATA, json).apply()
    }

    fun getUserData(): User? {
        val data = sharedPreferences.getString(DATA, null)

        if (data != null) {
            return gson.fromJson(data, User::class.java)
        }

        return null
    }

    fun saveSelectedCar(car: Car) {
        val json = gson.toJson(car)

        sharedPreferences.edit().putString(SELECTED_CAR, json).apply()
    }

    fun getSelectedCar(): Car? {
        val data = sharedPreferences.getString(SELECTED_CAR, null)

        if (data != null) {
            return gson.fromJson(data, Car::class.java)
        }

        return null
    }

    fun removeSelectedCar() {
        sharedPreferences.edit().remove(SELECTED_CAR).apply()
    }

    fun saveSelectedReservation(reservation: Reservation) {
        val json = gson.toJson(reservation)

        sharedPreferences.edit().putString(SELECTED_RESERVATION, json).apply()
    }

    fun getSelectedReservation(): Reservation? {
        val data = sharedPreferences.getString(SELECTED_RESERVATION, null)

        if (data != null) {
            return gson.fromJson(data, Reservation::class.java)
        }

        return null
    }

    fun removeSelectedReservation() {
        sharedPreferences.edit().remove(SELECTED_RESERVATION).apply()
    }

    fun saveSelectedUser(user: User) {
        val json = gson.toJson(user)

        sharedPreferences.edit().putString(SELECTED_USER, json).apply()
    }

    fun getSelectedUser(): User? {
        val data = sharedPreferences.getString(SELECTED_USER, null)

        if (data != null) {
            return gson.fromJson(data, User::class.java)
        }

        return null
    }

    fun removeSelectedUser() {
        sharedPreferences.edit().remove(SELECTED_USER).apply()
    }

    fun saveWithdrawDate(date: String) {
        sharedPreferences.edit().putString(SELECTED_DATE_W, date).apply()
    }

    fun getWithdrawDate(): String? {
        return sharedPreferences.getString(SELECTED_DATE_W, null)
    }

    fun saveDeliveryDate(date: String) {
        sharedPreferences.edit().putString(SELECTED_DATE_D, date).apply()
    }

    fun getDeliveryDate(): String? {
        return sharedPreferences.getString(SELECTED_DATE_D, null)
    }
}
