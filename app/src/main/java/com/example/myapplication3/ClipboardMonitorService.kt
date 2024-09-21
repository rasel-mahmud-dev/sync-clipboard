package com.example.myapplication3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat

class ClipboardMonitorService : Service() {
    private lateinit var clipboardManager: ClipboardManager
    private val handler = Handler()
    private var eventCount = 0

    override fun onCreate() {
        super.onCreate()

        Log.d("ClipboardMonitorService", "Service created")

        // Initialize ClipboardManager
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.addPrimaryClipChangedListener(clipboardListener)

        // Start foreground notification
        createNotificationChannel()
        val notification: Notification = NotificationCompat.Builder(this, "clipboard_channel")
            .setContentTitle("Clipboard Monitoring")
            .setContentText("Listening for clipboard changes")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
        startForeground(1, notification)

        // Start logging clipboard events
        startLogging()
    }

    private val clipboardListener = ClipboardManager.OnPrimaryClipChangedListener {
        if (clipboardManager.hasPrimaryClip()) {
            val clipData = clipboardManager.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val copiedText = clipData.getItemAt(0).text
                if (copiedText != null) {
                    eventCount++  // Increment the counter
                    Toast.makeText(this, "Copied text: $copiedText", Toast.LENGTH_SHORT).show()
                    Log.d("ClipboardMonitorService", "Copied text: $copiedText")
                }
            }
        }
    }

    private fun startLogging() {
        handler.post(object : Runnable {
            override fun run() {
                Log.d("ClipboardMonitorService", "Clipboard event count: $eventCount")
                handler.postDelayed(this, 2000)  // Log every 2 seconds
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        clipboardManager.removePrimaryClipChangedListener(clipboardListener)
        handler.removeCallbacksAndMessages(null)  // Stop logging when the service is destroyed
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    // Create notification channel for Android 8.0+
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "clipboard_channel",
                "Clipboard Monitoring",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}
