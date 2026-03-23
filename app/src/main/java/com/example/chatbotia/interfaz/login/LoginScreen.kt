package com.example.chatbotia.interfaz.login

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbotia.interfaz.ViewModelFactory
import com.example.chatbotia.interfaz.background.AnimatedRandomPaintBackground

@Composable
fun LoginScreen(
    onLoginSuccess: (isAdmin: Boolean) -> Unit,  // ← Cambio aquí
    onGoToRegister: () -> Unit
) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel(
        factory = ViewModelFactory(context)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedRandomPaintBackground()

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoginContent(
                viewModel = loginViewModel,
                onLoginSuccess = onLoginSuccess,  // ← Pasar el callback
                onGoToRegister = onGoToRegister
            )
        }
    }
}
