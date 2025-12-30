package com.example.chatbotia.interfaz.chat

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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

/* ============================
   📦 MODELO DE DATOS
   ============================ */
data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

/* ============================
   💬 PANTALLA DE CHAT
   ============================ */
@Composable
fun ChatScreen() {
    val messages = remember {
        mutableStateListOf(
            ChatMessage("¡Hola! Soy Seren 👋 ¿Cómo te sientes hoy?", false)
        )
    }

    var isTyping by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Scroll automático al último mensaje
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding() // ⌨️ Sincroniza toda la UI con el teclado
    ) {
        // 🔮 Fondo Animado
        AnimatedRandomPaintBackground()

        // 🧾 Card del Chat: Anclada ABAJO para que el input quede siempre sobre el teclado
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .padding(bottom = 16.dp), // Margen inferior estético
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                
                /* ---------- HEADER ---------- */
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(VioletPrimary)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Seren",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                /* ---------- MENSAJES ---------- */
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { message ->
                        ChatBubble(message)
                    }

                    if (isTyping) {
                        item { TypingIndicator() }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )

                /* ---------- INPUT ---------- */
                ChatInput(
                    text = inputText,
                    onTextChange = { inputText = it },
                    onSend = {
                        if (inputText.isNotBlank()) {
                            val userText = inputText
                            messages.add(ChatMessage(userText, true))
                            inputText = ""
                            isTyping = true

                            scope.launch {
                                delay(1500)
                                isTyping = false
                                messages.add(ChatMessage(getMockResponse(userText), false))
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    val color = if (message.isUser) VioletPrimary else Color(0xFFEEEEEE)
    val textColor = if (message.isUser) Color.White else Color.Black
    val shape = RoundedCornerShape(
        topStart = 16.dp, topEnd = 16.dp,
        bottomStart = if (message.isUser) 16.dp else 0.dp,
        bottomEnd = if (message.isUser) 0.dp else 16.dp
    )

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Surface(color = color, shape = shape) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = textColor,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun TypingIndicator() {
    val transition = rememberInfiniteTransition(label = "typing")
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(Color(0xFFF1F1F1), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .padding(2.dp)
                    .background(VioletPrimary.copy(alpha = alpha), CircleShape)
            )
        }
    }
}

@Composable
fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje...", color = Color.Gray) },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VioletPrimary,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            IconButton(
                onClick = onSend,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(YellowAccent, CircleShape)
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

fun getMockResponse(input: String): String {
    val lower = input.lowercase()
    return when {
        lower.contains("hola") -> "¡Hola! ¿Cómo has estado?"
        lower.contains("triste") || lower.contains("mal") -> "Te escucho. Desahogarte es el primer paso. ¿Qué pasó?"
        else -> "Entiendo. Cuéntame más sobre eso, te leo con atención."
    }
}
