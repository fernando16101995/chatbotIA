package com.example.chatbotia.interfaz.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbotia.interfaz.background.AnimatedRandomPaintBackground
import com.example.chatbotia.interfaz.theme.VioletPrimary
import com.example.chatbotia.interfaz.theme.YellowAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val time: String = "18:20"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val messages = remember { 
        mutableStateListOf(
            ChatMessage("¡Hola! Soy Seren. ¿Cómo te sientes hoy?", false, "18:20")
        ) 
    }
    var inputText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Scroll automático al último mensaje
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 🔮 Fondo animado estable
        AnimatedRandomPaintBackground()

        // 🧾 Contenedor exterior FIJO (No tiene imePadding, por eso la Card no salta)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(), // Llena el espacio pero no se mueve
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    
                    // Cabecera estática (Siempre arriba)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = VioletPrimary
                    ) {
                        Text(
                            text = "Seren",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    // Lista de mensajes (Usa weight para encogerse cuando el input sube)
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(messages) { message ->
                            ChatBubble(message)
                        }
                    }

                    Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.5.dp)

                    // Barra de entrada: AQUÍ aplicamos el imePadding
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .imePadding(), // ⌨️ SOLO esta parte reacciona al teclado
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Escribe algo...", color = Color.Gray) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = VioletPrimary,
                                unfocusedBorderColor = Color.Gray,
                                cursorColor = VioletPrimary
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    val text = inputText
                                    messages.add(ChatMessage(text, true, "18:22"))
                                    inputText = ""
                                    scope.launch {
                                        delay(600)
                                        messages.add(ChatMessage(getMockResponse(text), false, "18:23"))
                                    }
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = YellowAccent)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Enviar",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isUser) VioletPrimary else Color(0xFFF1F1F1)
    val textColor = if (message.isUser) Color.White else Color.Black

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (message.isUser) 12.dp else 0.dp,
                bottomEnd = if (message.isUser) 0.dp else 12.dp
            ),
            tonalElevation = 2.dp
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = textColor,
                fontSize = 15.sp
            )
        }
    }
}

fun getMockResponse(input: String): String {
    val lower = input.lowercase()
    return when {
        lower.contains("hola") -> "¡Hola! ¿En qué puedo ayudarte hoy?"
        lower.contains("triste") -> "Siento que estés pasando por esto. Cuéntame más."
        else -> "Te escucho con atención."
    }
}
