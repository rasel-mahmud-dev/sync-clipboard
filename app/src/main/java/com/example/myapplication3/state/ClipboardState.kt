package com.example.myapplication3.state

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson


data class Clip(
    val clipContent: String,
    val type: String,
    val time: String,
    val userId: String,
    val timestamp: Long,
    val sharedWith: List<String>
)

object ClipboardState {
    private const val PREFS_NAME = "clipboard_prefs"
    private const val KEY_CLIPS = "clips"

    private val gson = Gson()

    var clips: List<Clip> = emptyList()

    // Save list of clips globally and persist in SharedPreferences
    fun setClipboardInfo(clips: List<Clip>, context: Context) {
        this.clips = clips
        saveToPreferences(context)
    }

    // Load the list of clips from SharedPreferences
    fun loadFromPreferences(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val clipsJson = prefs.getString(KEY_CLIPS, null)
        if (clipsJson != null) {
            val type = object : TypeToken<List<Clip>>() {}.type
            clips = gson.fromJson(clipsJson, type) ?: emptyList()
        }
    }

    // Save the clips data to SharedPreferences
    private fun saveToPreferences(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val clipsJson = gson.toJson(clips)
        with(prefs.edit()) {
            putString(KEY_CLIPS, clipsJson)
            apply()
        }
    }

    // Clear the clipboard info
    fun clearClipboardInfo(context: Context) {
        this.clips = emptyList()
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            clear()
            apply()
        }
    }
}
