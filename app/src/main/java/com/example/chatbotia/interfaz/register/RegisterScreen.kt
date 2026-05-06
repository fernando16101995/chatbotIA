package com.example.chatbotia.interfaz.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbotia.interfaz.ViewModelFactory
import com.example.chatbotia.interfaz.theme.*

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackToLogin) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(padding)
                .padding(horizontal = 32.dp)
                .verticalScroll(scrollState)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary
            )

            Text(
                text = "Únete a Seren",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Correo electrónico", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                TextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    singleLine = true,
                    enabled = !viewModel.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = AppSurfaceVariant,
                        unfocusedContainerColor = AppSurface,
                        focusedIndicatorColor = AccentPrimary,
                        unfocusedIndicatorColor = DividerColor,
                        cursorColor = AccentPrimary
                    ),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
            }

            // Password
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Contraseña", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                TextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    enabled = !viewModel.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = AppSurfaceVariant,
                        unfocusedContainerColor = AppSurface,
                        focusedIndicatorColor = AccentPrimary,
                        unfocusedIndicatorColor = DividerColor,
                        cursorColor = AccentPrimary
                    ),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
            }

            // Confirm Password
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Confirmar contraseña", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                TextField(
                    value = viewModel.confirmPassword,
                    onValueChange = { viewModel.confirmPassword = it },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    enabled = !viewModel.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = AppSurfaceVariant,
                        unfocusedContainerColor = AppSurface,
                        focusedIndicatorColor = AccentPrimary,
                        unfocusedIndicatorColor = DividerColor,
                        cursorColor = AccentPrimary
                    ),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
            }

            AnimatedVisibility(visible = viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage ?: "",
                    color = ErrorColor,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }

            AnimatedVisibility(visible = viewModel.successMessage != null) {
                Text(
                    text = viewModel.successMessage ?: "",
                    color = SuccessColor,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = { viewModel.register(onRegisterSuccess) },
                enabled = !viewModel.isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentPrimary,
                    disabledContainerColor = AccentPrimary.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = TextPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Registrarse",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary
                    )
                }
            }

            TextButton(onClick = onBackToLogin) {
                Text(
                    "¿Ya tienes cuenta? Inicia sesión",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AccentPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
