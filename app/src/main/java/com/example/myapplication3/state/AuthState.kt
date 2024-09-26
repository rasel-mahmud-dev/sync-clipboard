package com.example.myapplication3.state

import android.content.Context
import android.content.SharedPreferences

object AuthState {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_EMAIL = "email"
    private const val KEY_USERNAME = "username"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    var email: String? = null
    var username: String? = null
    var isLoggedIn: Boolean = false

    fun setAuthInfo(email: String, username: String, context: Context) {
        this.email = email
        this.username = username
        this.isLoggedIn = true

        saveToPreferences(context)
    }

    fun clearAuthInfo(context: Context) {
        this.email = null
        this.username = null
        this.isLoggedIn = false

        clearPreferences(context)
    }

    private fun saveToPreferences(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_USERNAME, username)
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)

        editor.apply() // Save the data asynchronously
    }

    private fun clearPreferences(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.clear() // Clear all saved data
        editor.apply()
    }

    // Load the login info from preferences when the app starts
    fun loadFromPreferences(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        email = sharedPreferences.getString(KEY_EMAIL, null)
        username = sharedPreferences.getString(KEY_USERNAME, null)
        isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        return isLoggedIn
    }
}
