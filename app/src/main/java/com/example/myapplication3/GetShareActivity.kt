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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication3.firebase.FirestoreInstance
import java.util.Date

class GetShareActivity : ComponentActivity() {
    private var receivedLink: String? = null
    private val date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the incoming intent when the activity is created
        handleIncomingIntent(intent)

        // Set your content view or compose UI here
        setContent {
            MyAppUI(receivedLink)
        }
    }

    private fun handleAddText(content: String, context: android.content.Context) {
        val clipData = hashMapOf(
            "userId" to "userId1",
            "content" to content,
            "timestamp" to System.currentTimeMillis(),
            "sharedWith" to listOf("linux_pc", "windows pc")
        )

        val firestore = FirestoreInstance.getInstance
        val clipsCollection = firestore.collection("clips")

        clipsCollection.add(clipData)
            .addOnSuccessListener { documentReference ->
                // After adding, fetch the document data
                documentReference.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            Toast.makeText(context, "Document added successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to retrieve document: $e", Toast.LENGTH_SHORT).show()
                        Log.e("GetShareActivity", "Failed to retrieve document: $e")
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error adding document: $e", Toast.LENGTH_SHORT).show()
                Log.e("GetShareActivity", "Error adding document: $e")
            }
    }

    private fun handleIncomingIntent(intent: Intent) {
        if (Intent.ACTION_SEND == intent.action && intent.type == "text/plain") {
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            text?.let {
                receivedLink = it
                Log.d("GetShareActivity", "Received link: $it")
                handleAddText(receivedLink ?: "", this) // Pass context
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
        val context = LocalContext.current // Get the context inside the composable
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
