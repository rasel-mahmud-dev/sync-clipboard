package com.example.myapplication3.routes

import androidx.navigation.compose.NavHost
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.myapplication3.screens.ClipListScreen
import com.example.myapplication3.screens.LoginScreen
import com.example.myapplication3.screens.RegistrationScreen
import com.example.myapplication3.screens.SettingsScreen

object Routes {
    @Composable
    fun init(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") { entry ->
                LoginScreen(navController)
            }

            composable("clip") { entry ->
                ClipListScreen(navController)
            }

            composable("registration") { entry ->
                RegistrationScreen(navController)
            }

            composable("settings") { entry ->
                SettingsScreen(navController)
            }
        }
    }
}
