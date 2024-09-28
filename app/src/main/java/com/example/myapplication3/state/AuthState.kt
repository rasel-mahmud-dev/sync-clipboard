package com.example.myapplication3.state

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myapplication3.MainActivity
import com.google.gson.Gson

data class Auth(
    val email: String,
    val _id: String,
    val deviceId: String,
    val username: String,
    var token: String?,
    var devices: List<Device>?
)

data class Device(
    val _id: String,
    val device: String,
    val userId: String,
)

object AuthState {

    val sharedPreferences = MainActivity.prefs
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_AUTH = "auth"
    private const val KEY_TOKEN = "auth_token"

    var auth by mutableStateOf<Auth?>(null)

    fun setAuthInfo(auth: Auth?) {
        this.auth = auth
        saveToPreferences()
    }

    fun setAuthDevices(devices: List<Device>?) {
        this.auth?.devices = devices
    }

    fun clearAuthInfo() {
        this.auth = null
        clearPreferences()
    }

    private fun saveToPreferences() {

        val editor = sharedPreferences.edit()

        val gson = Gson()
        if (auth != null) {
            val authJson = gson.toJson(auth)
            editor.putString(KEY_AUTH, authJson)
            editor.putString(KEY_TOKEN, auth!!.token)
        } else {
            editor.remove(KEY_AUTH)
            editor.remove(KEY_TOKEN)
        }
        editor.apply()
    }

    private fun clearPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear() // Clear all saved data
        editor.apply()
    }

    fun loadAuthFromPreferences(): Auth? {
        val sharedPreferences = MainActivity.prefs
        val gson = Gson()

        // Retrieve the JSON string for the Auth object
        val authJson = sharedPreferences.getString(KEY_AUTH, null)

        return if (authJson != null) {
            gson.fromJson(authJson, Auth::class.java)?.apply {
                this.token = sharedPreferences.getString(KEY_TOKEN, null)
            }
        } else {
            null
        }
    }

    fun getToken(): String? {
        if (auth?.token == null) {
            return auth?.token
        } else {
            loadAuthFromPreferences()
            return auth?.token
        }
    }
}
