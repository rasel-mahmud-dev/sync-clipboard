package com.example.myapplication3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class YourReceivingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the incoming intent when the activity is created
        handleIncomingIntent(intent)

        // Set your content view or compose UI here
        setContent {
            // Your UI code
        }
    }

    private fun handleIncomingIntent(intent: Intent) {
        if (Intent.ACTION_SEND == intent.action && intent.type == "text/plain") {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            text?.let {
                // Process the received text (link)
                Log.d("hisdfhsd", "aWESOME LIST::: kLAlsdlkfjsldkfjsd")
                Toast.makeText(this, "Received link: $it", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        intent?.let { handleIncomingIntent(it) }
    }
}
