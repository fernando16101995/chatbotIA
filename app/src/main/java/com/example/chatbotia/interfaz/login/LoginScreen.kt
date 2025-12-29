package com.example.chatbotia.interfaz.login

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.chatbotia.interfaz.background.AnimatedRandomPaintBackground

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 🔮 Fondo animado
        AnimatedRandomPaintBackground()

        // 🧾 Contenido del login
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoginContent(
                onLoginSuccess = onLoginSuccess,
                onGoToRegister = onGoToRegister
            )
        }
    }
}
