package com.example.chatbotia.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatbotia.data.model.RetrofitClient
import com.example.chatbotia.data.model.TokenManager
import com.example.chatbotia.interfaz.dashboard.DashboardScreen
import com.example.chatbotia.interfaz.dashboard.DashboardViewModel
import com.example.chatbotia.interfaz.dashboard.DashboardViewModelFactory
import com.example.chatbotia.interfaz.login.LoginScreen
import com.example.chatbotia.interfaz.navigation.MainScaffold
import com.example.chatbotia.interfaz.register.RegisterScreen
import com.example.chatbotia.interfaz.dashboard.UsersManagementScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "login",
        enterTransition = { fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) },
        exitTransition = { fadeOut(animationSpec = androidx.compose.animation.core.tween(300)) }
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { isAdmin ->
                    if (isAdmin) {
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("main") {
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

        composable("main") {
            MainScaffold(
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
                onBackClick = { navController.popBackStack()},
                onManageUsersClick = { navController.navigate("Users_management")}

            )
        }

        composable("users_management") {
            val tokenManager = TokenManager(context)
            val token = tokenManager.getToken() ?: ""
            val factory = DashboardViewModelFactory(RetrofitClient.api)
            val dashboardViewModel: DashboardViewModel = viewModel(factory = factory)

            UsersManagementScreen(
                viewModel = dashboardViewModel,
                token = token,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
