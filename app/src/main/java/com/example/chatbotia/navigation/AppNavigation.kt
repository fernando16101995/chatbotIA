package com.example.chatbotia.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatbotia.interfaz.chat.ChatScreen
import com.example.chatbotia.interfaz.login.LoginScreen
import com.example.chatbotia.interfaz.profile.ProfileScreen
import com.example.chatbotia.interfaz.register.RegisterScreen

import com.example.chatbotia.interfaz.dashboard.DashboardScreen
import com.example.chatbotia.interfaz.dashboard.DashboardViewModel
import com.example.chatbotia.interfaz.dashboard.DashboardViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbotia.data.model.RetrofitClient
import com.example.chatbotia.data.model.TokenManager

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { isAdmin ->
                    if (isAdmin) {
                        // ✅ Ahora coincide con la ruta definida abajo
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("chat") {
                            popUpTo("login") { inclusive = true }
                        }
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
            ChatScreen(
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            val tokenManager = TokenManager(context)
            val token = tokenManager.getToken() ?: ""
            
            val factory = DashboardViewModelFactory(RetrofitClient.api)
            val dashboardViewModel: DashboardViewModel = viewModel(factory = factory)

            DashboardScreen(
                viewModel = dashboardViewModel,
                token = token,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
