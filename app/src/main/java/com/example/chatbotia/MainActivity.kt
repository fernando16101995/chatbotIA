package com.example.chatbotia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.chatbotia.navigation.AppNavigation
import com.example.chatbotia.interfaz.theme.ChatbotIATheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ⌨️ Activa el soporte para teclado y pantalla completa
        enableEdgeToEdge()

        setContent {
            ChatbotIATheme {
                AppNavigation()
            }
        }
    }
}
