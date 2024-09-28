package com.example.myapplication3.screens

import android.util.Log
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
import com.example.myapplication3.MainActivity
import com.example.myapplication3.apis.BASE_URL
import com.example.myapplication3.firebase.FirestoreInstance
import com.example.myapplication3.http.ApiResponse
import com.example.myapplication3.http.LoginApiResponse
import com.example.myapplication3.http.client
import com.example.myapplication3.state.Auth
import com.example.myapplication3.state.AuthState
import com.google.gson.Gson
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.launch

data class User(
    val username: String,
    val avatar: String,
    val email: String,
    val password: String,
)

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf(TextFieldValue("rasel.mahmud.dev@gmail.com")) }
    var password by remember { mutableStateOf(TextFieldValue("123")) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

//    val firestore = FirestoreInstance.getInstance
    val coroutineScope = rememberCoroutineScope()

    data class LoginPayload(val email: String, val password: String, val device: String)


    suspend fun fetchUserDevice() {
        try {
            val response: HttpResponse = client.get("$BASE_URL/api/v1/auth/devices") {
                contentType(ContentType.Application.Json)
            }

            if (response.status.value == 200) {
                Toast.makeText(context, "fetch success", Toast.LENGTH_SHORT).show()
                val result: String = response.bodyAsText()
                println(result)

//                AuthState.setAuthDevices(context)

            }

        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun handleLogin() {
        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(context, "All fields are required.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val response: HttpResponse = client.post("$BASE_URL/api/v1/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(
                    LoginPayload(
                        email = email.text, password = password.text, device = "Xiaomi"
                    )
                )
            }

            if (response.status.value == 200) {
                Toast.makeText(context, "fetch success", Toast.LENGTH_SHORT).show()
                val result: LoginApiResponse = response.body()
                AuthState.setAuthInfo(
                    Auth(
                        username = result.data.user.username,
                        _id = result.data.user._id,
                        deviceId = result.data.user.deviceId,
                        email = result.data.user.email,
                        token = result.data.session.token,
                        devices = null
                    )
                )
                fetchUserDevice()
//               navController.navigate("home")

            } else {
                Toast.makeText(
                    context, "Failed to fetch user. Status code:", Toast.LENGTH_SHORT
                ).show()
                println("Failed to fetch user. Status code : ${response.status.value}")
            }

        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }


//        firestore.collection("users")
//            .document(email.text)
//            .get()
//            .addOnSuccessListener { documentSnapshot ->
//                if (!documentSnapshot.exists()) {
//                    Toast.makeText(context, "User not found.", Toast.LENGTH_SHORT).show()
//                    return@addOnSuccessListener
//                }
//
//                Log.d("TAG", "handleLogin: ")
//                val email = documentSnapshot.data?.get("email").toString()
//                val username = documentSnapshot.data?.get("username").toString()
//
//                AuthState.setAuthInfo(
//                    email,
//                    username,
//                    context
//                )
//
//                navHost.navigate("clip")
//            }
//            .addOnFailureListener { e ->
//                Log.d("error", e.toString())
//                Toast.makeText(context, "Error logging in.", Toast.LENGTH_SHORT).show()
//            }

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
                text = "Login", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black
            )

            Text(AuthState.auth?.email.toString())
            Text("AuthState.auth?.email.toString(")

            Spacer(modifier = Modifier.height(24.dp))

            // Email Input Field
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                modifier = Modifier
                    .background(
                        Color(0xFFE0E0E0), shape = MaterialTheme.shapes.medium
                    )
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
                    .background(
                        Color(0xFFE0E0E0), shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
                    .fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = { coroutineScope.launch { handleLogin() } },
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
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { /* Handle help click */ }) {
                    Text(text = "Need Help?", color = Color(0xFFFF5252))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "You are not a member? ", color = Color.Gray, fontSize = 14.sp
            )

            TextButton(onClick = { navController.navigate("registration") }) {
                Text(text = "Register", color = Color(0xFFFF5252))
            }
        }
    }
}

private fun authenticate(email: String, password: String): Boolean {
    return email == "user" && password == "pass"
}
