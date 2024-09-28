package com.example.myapplication3.http


import android.util.Log
import com.example.myapplication3.MainActivity
import com.example.myapplication3.state.AuthState
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson
import java.util.Date
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }

    install(DefaultRequest) {
        val token = AuthState.getToken()
        if (!headers.contains("No-Authentication") && token !== null) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}

data class LoginApiResponse(
    val message: String,
    val data: LoginData
) {
    data class LoginData(
        val user: User,
        val session: Session
    ) {
        data class User(
            val _id: String,
            val device: String,
            val username: String,
            val email: String,
            val password: String,
            val avatar: String,
            val createdAt: String,
            val status: String,
            val deviceId: String
        )

        data class Session(
            val _id: String,
            val userId: String,
            val device: String,
            val token: String,
            val createdAt: String
        )
    }
}
