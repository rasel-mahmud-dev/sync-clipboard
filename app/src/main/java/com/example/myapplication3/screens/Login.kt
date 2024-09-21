package com.example.myapplication3.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController



@Composable
fun LoginScreen(navHost: NavHostController) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    fun handleLogin(){
        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(context, "All fields are required.", Toast.LENGTH_SHORT).show()
            return
        }


    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Hello There!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email Input Field
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                modifier = Modifier
                    .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
                    .fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input Field
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                modifier = Modifier
                    .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
                    .fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = { handleLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                shape = MaterialTheme.shapes.medium

            ) {
                Text("Login", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { /* Handle help click */ }) {
                    Text(text = "Need Help?", color = Color(0xFFFF5252))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "You are not a member? ",
                color = Color.Gray,
                fontSize = 14.sp
            )

            TextButton(onClick = { navHost.navigate("Registration") }) {
                Text(text = "Register", color = Color(0xFFFF5252))
            }
        }
    }
}

private fun authenticate(email: String, password: String): Boolean {
    return email == "user" && password == "pass"
}
