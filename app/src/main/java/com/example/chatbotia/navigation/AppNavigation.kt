package com.example.chatbotia.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatbotia.interfaz.chat.ChatScreen
import com.example.chatbotia.interfaz.login.LoginScreen
import com.example.chatbotia.interfaz.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("chat") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()

                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("chat") {
            ChatScreen()
        }
    }
}
