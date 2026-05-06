package com.example.chatbotia.interfaz.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
            .background(AppBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Seren",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Tu compañero de bienestar",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Correo electrónico",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
                TextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    singleLine = true,
                    enabled = !viewModel.isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = AppSurfaceVariant,
                        unfocusedContainerColor = AppSurface,
                        focusedIndicatorColor = AccentPrimary,
                        unfocusedIndicatorColor = DividerColor,
                        cursorColor = AccentPrimary,
                        focusedLabelColor = AccentPrimary,
                        unfocusedLabelColor = TextSecondary
                    ),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
            }

            // Password field
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Contraseña",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
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

            AnimatedVisibility(visible = viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage ?: "",
                    color = ErrorColor,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = { viewModel.login(onLoginSuccess) },
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
                        "Iniciar sesión",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary
                    )
                }
            }

            TextButton(onClick = onGoToRegister) {
                Text(
                    "¿No tienes cuenta? Regístrate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AccentPrimary
                )
            }
        }
    }
}
