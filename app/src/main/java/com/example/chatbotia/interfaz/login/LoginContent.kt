package com.example.chatbotia.interfaz.login

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
import com.example.chatbotia.interfaz.background.AnimatedRandomPaintBackground
import com.example.chatbotia.interfaz.theme.VioletPrimary
import com.example.chatbotia.interfaz.theme.YellowAccent

@Composable
fun LoginContent(
    viewModel: LoginViewModel,
    onLoginSuccess: (isAdmin: Boolean) -> Unit,
    onGoToRegister: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 🔮 Fondo animado compartido
        AnimatedRandomPaintBackground()

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Seren",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletPrimary
                )

                Text(
                    text = "Inicia sesión",
                    color = VioletPrimary
                )

                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !viewModel.isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = VioletPrimary,
                        focusedLabelColor = VioletPrimary,
                        cursorColor = VioletPrimary
                    )
                )

                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !viewModel.isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = VioletPrimary,
                        focusedLabelColor = VioletPrimary,
                        cursorColor = VioletPrimary
                    )
                )

                if (viewModel.errorMessage != null) {
                    Text(
                        text = viewModel.errorMessage!!,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Button(
                    onClick = { viewModel.login(onLoginSuccess) },
                    enabled = !viewModel.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowAccent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Entrar",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                // 👉 Ir a registro
                ClickableText(
                    text = AnnotatedString("¿No tienes cuenta? Regístrate"),
                    onClick = { onGoToRegister() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
