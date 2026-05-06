package com.example.chatbotia.interfaz.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbotia.interfaz.ViewModelFactory

@Composable
fun LoginScreen(
    onLoginSuccess: (isAdmin: Boolean) -> Unit,
    onGoToRegister: () -> Unit
) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel(
        factory = ViewModelFactory(context)
    )

    LoginContent(
        viewModel = loginViewModel,
        onLoginSuccess = onLoginSuccess,
        onGoToRegister = onGoToRegister
    )
}
