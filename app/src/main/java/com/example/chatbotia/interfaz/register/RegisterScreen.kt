package com.example.chatbotia.interfaz.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbotia.interfaz.ViewModelFactory
import com.example.chatbotia.interfaz.components.MinimalButton
import com.example.chatbotia.interfaz.components.MinimalTextField
import com.example.chatbotia.interfaz.theme.*

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel(
        factory = ViewModelFactory(context)
    )
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header con botón de atrás discreto
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToLogin) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = TextPrimary)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.headlineLarge,
                    color = AccentViolet
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Empieza tu camino hacia el bienestar.",
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

                MinimalTextField(
                    value = viewModel.confirmPassword,
                    onValueChange = { viewModel.confirmPassword = it },
                    label = "Confirmar contraseña",
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

            if (viewModel.successMessage != null) {
                Text(
                    text = viewModel.successMessage!!,
                    color = AccentBlue,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            MinimalButton(
                text = "Registrarse",
                onClick = { viewModel.register(onRegisterSuccess) },
                isLoading = viewModel.isLoading
            )

            ClickableText(
                text = AnnotatedString("¿Ya tienes cuenta? Inicia sesión"),
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                onClick = { onBackToLogin() }
            )
        }
    }
}
