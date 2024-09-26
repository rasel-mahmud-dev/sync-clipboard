package com.example.myapplication3

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.myapplication3.firebase.FirestoreInstance
import com.example.myapplication3.routes.Routes
import com.example.myapplication3.state.AuthState

class MainActivity : ComponentActivity() {

    val firestore = FirestoreInstance.getInstance
    val messagesCollection = firestore.collection("messages")

    val messageData = mapOf(
        "sender" to "sender_id",
        "recipient" to "recipient_id",
        "timestamp" to System.currentTimeMillis(),
        "content" to "This is a message"
    )

    private fun sendMessage() {
        messagesCollection.add(messageData)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Document written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error writing document: ${e.message}")
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                innerPadding

                val navController = rememberNavController()

                Routes.init(navController = navController)

                // Load auth info from SharedPreferences when the app starts
                val isLogged = AuthState.loadFromPreferences(this)
                if (isLogged) navController.navigate("clip")

//                Column {
//                    Text("Clipboard monitoring service is off.")
//                    CopyableText("Clipboard monitoring service is off copyu.")
//                    Button(onClick = { startClipboardService() }) {
//                        Text("Start Clipboard Service")
//                    }
//
//                    Button(onClick = { sendMessage() }) {
//                        Text("Send message on firebase")
//                    }
//
//                    if (AuthState.isLoggedIn) {
//                        Text("Logged ${AuthState.email}")
//                        // Navigate to the home screen or dashboard
////                    navController.navigate("Dashboard")
//                    } else {
//                        // Show login screen
////                    navController.navigate("Login")
//                    }
//                }

            }
        }
    }

    private fun startClipboardService() {
//        val serviceIntent = Intent(this, ClipboardMonitorService::class.java)
//        Log.d("sd", "sdf")
//        startForegroundService(serviceIntent)
    }
}

@Composable
fun CopyableText(text: String) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    // Make the text selectable and copyable
    SelectionContainer {
        Text(
            text = text,
            modifier = Modifier.clickable {
                // Copy text to clipboard when clicked
                clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(text))
                Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

//@Composable
//fun LoadAuthInfo() {
//    val context = LocalContext.current
//
//    LaunchedEffect(Unit) {
//        withContext(Dispatchers.IO) {
//            val authUser = AuthPreferences.getAuthUser(context)
//            if (authUser != null) {
//                GlobalAuthState.authUser = authUser
//            } else {
////                GlobalAuthState.authUser = authUser
////                print("Please login...")
//            }
//        }
//    }
//}
