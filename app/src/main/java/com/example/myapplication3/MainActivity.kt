package com.example.myapplication3

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                innerPadding
                Column {
                    Text("Clipboard monitoring service is off.")
                    CopyableText("Clipboard monitoring service is off copyu.")
                    Button(onClick = { startClipboardService() }) {
                        Text("Start Clipboard Service")
                    }
                }
            }
        }
    }

    private fun startClipboardService() {
        val serviceIntent = Intent(this, ClipboardMonitorService::class.java)
        Log.d("sd", "sdf")
        startForegroundService(serviceIntent)
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