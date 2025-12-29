package com.example.chatbotia.interfaz.chat


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ChatBot IA 🤖") }
            )
        }
    ) { padding ->
        Text(
            text = "Bienvenido al chat",
            modifier = Modifier.padding(padding)
        )
    }
}
