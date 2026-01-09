package com.example.chatbotia.interfaz.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbotia.interfaz.ViewModelFactory
import com.example.chatbotia.interfaz.background.AnimatedRandomPaintBackground
import com.example.chatbotia.interfaz.theme.VioletPrimary
import com.example.chatbotia.interfaz.theme.YellowAccent

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
            .imePadding() // ⌨️ Mueve la UI arriba del teclado
    ) {
        // 🔮 Fondo animado
        AnimatedRandomPaintBackground()

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            // 📜 Column con Scroll para que nada se pierda al abrir el teclado
            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Seren",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletPrimary
                )

                Text(
                    text = "Crear cuenta",
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

                OutlinedTextField(
                    value = viewModel.confirmPassword,
                    onValueChange = { viewModel.confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
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

                if (viewModel.successMessage != null) {
                    Text(
                        text = viewModel.successMessage!!,
                        color = Color.Green,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Button(
                    onClick = { viewModel.register(onRegisterSuccess) },
                    enabled = !viewModel.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = YellowAccent),
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
                            "Registrarse",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                ClickableText(
                    text = AnnotatedString("¿Ya tienes cuenta? Inicia sesión"),
                    onClick = { onBackToLogin() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
