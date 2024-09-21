package com.example.myapplication3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

class YourReceivingActivity : ComponentActivity() {
    private var receivedLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the incoming intent when the activity is created
        handleIncomingIntent(intent)

        // Set your content view or compose UI here
        setContent {
            MyAppUI(receivedLink)
        }
    }

    private fun handleIncomingIntent(intent: Intent) {
        if (Intent.ACTION_SEND == intent.action && intent.type == "text/plain") {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            text?.let {
                receivedLink = it
                Log.d("hisdfhsd", "aWESOME LIST::: kLAlsdlkfjsldkfjsd")
                Toast.makeText(this, "Received link: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        intent?.let { handleIncomingIntent(it) }
    }


    @Composable
    fun MyAppUI(link: String?) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (link != null) {
                    Text(
                        text = "Received Link:",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = link,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(text = "No link received.")
                }

                Button(onClick = { finish() }) {
                    Text("Close App")
                }
            }
        }
    }

}