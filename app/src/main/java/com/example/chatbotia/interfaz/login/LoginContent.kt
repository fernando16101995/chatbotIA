package com.example.chatbotia.interfaz.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbotia.interfaz.components.MinimalButton
import com.example.chatbotia.interfaz.components.MinimalTextField
import com.example.chatbotia.interfaz.theme.*

@Composable
fun LoginContent(
    viewModel: LoginViewModel,
    onLoginSuccess: (isAdmin: Boolean) -> Unit,
    onGoToRegister: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Seren",
                    style = MaterialTheme.typography.headlineLarge,
                    color = AccentViolet
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Bienvenido de nuevo.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MinimalTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = "Correo electrónico",
                    enabled = !viewModel.isLoading
                )

                MinimalTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = "Contraseña",
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !viewModel.isLoading
                )
            }

            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = ErrorRed,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            MinimalButton(
                text = "Iniciar Sesión",
                onClick = { viewModel.login(onLoginSuccess) },
                isLoading = viewModel.isLoading
            )

            ClickableText(
                text = AnnotatedString("¿No tienes cuenta? Regístrate"),
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                onClick = { onGoToRegister() }
            )
        }
    }
}
