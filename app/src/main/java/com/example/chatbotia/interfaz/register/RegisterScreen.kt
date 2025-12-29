package com.example.chatbotia.interfaz.register

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
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔮 MISMO fondo animado
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
                    text = "Crear cuenta",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletPrimary
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = VioletPrimary,
                        focusedLabelColor = VioletPrimary,
                        cursorColor = VioletPrimary
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = VioletPrimary,
                        focusedLabelColor = VioletPrimary,
                        cursorColor = VioletPrimary
                    )
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = VioletPrimary,
                        focusedLabelColor = VioletPrimary,
                        cursorColor = VioletPrimary
                    )
                )

                Button(
                    onClick = {
                        if (
                            email.isNotBlank() &&
                            password.isNotBlank() &&
                            password == confirmPassword
                        ) {
                            onRegisterSuccess()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowAccent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        "Registrarse",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
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
