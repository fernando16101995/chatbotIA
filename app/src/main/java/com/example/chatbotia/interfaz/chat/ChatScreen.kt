package com.example.chatbotia.interfaz.chat

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbotia.interfaz.ViewModelFactory
import com.example.chatbotia.interfaz.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onNavigateToProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: ChatViewModel = viewModel(factory = ViewModelFactory(context))
    val listState = rememberLazyListState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Scroll automático al final cuando llega un mensaje nuevo
    LaunchedEffect(viewModel.messages.size) {
        if (viewModel.messages.isNotEmpty()) {
            listState.animateScrollToItem(viewModel.messages.size - 1)
        }
    }

    // Scroll automático mientras el bot escribe (streaming)
    if (viewModel.isTyping && viewModel.messages.isNotEmpty()) {
        val lastMessage = viewModel.messages.last()
        LaunchedEffect(lastMessage.text.length) {
            listState.scrollToItem(viewModel.messages.size - 1)
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = SurfaceDark,
            title = { Text("Limpiar conversación", color = TextPrimary) },
            text = { Text("¿Deseas eliminar el historial de este chat?", color = TextSecondary) },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.clearChat()
                    showDeleteDialog = false 
                }) {
                    Text("Limpiar", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        )
    }

    Scaffold(
        containerColor = BgDark,
        topBar = {
            TopAppBar(
                title = { 
                    Text("Seren", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = TextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BgDark,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.messages) { message ->
                    val isLast = viewModel.messages.lastOrNull() == message
                    ChatBubble(
                        message = message, 
                        isStreaming = viewModel.isTyping && isLast && !message.isUser
                    )
                }
            }

            ChatInput(
                text = viewModel.inputText,
                onTextChange = { viewModel.inputText = it },
                onSend = { viewModel.sendMessage() },
                isLoading = viewModel.isTyping
            )
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage, isStreaming: Boolean) {
    val horizontalAlignment: Alignment.Horizontal = if (message.isUser) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isUser) AccentViolet else SurfaceVariant
    val textColor = if (message.isUser) Color.White else TextPrimary
    val shape = RoundedCornerShape(
        topStart = 16.dp, 
        topEnd = 16.dp,
        bottomStart = if (message.isUser) 16.dp else 4.dp,
        bottomEnd = if (message.isUser) 4.dp else 16.dp
    )

    Column(
        modifier = Modifier.fillMaxWidth(), 
        horizontalAlignment = horizontalAlignment
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(shape)
                .background(bubbleColor)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            SelectionContainer {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = message.text,
                        color = textColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (isStreaming) {
                        StreamingCursor()
                    }
                }
            }
        }
    }
}

@Composable
fun StreamingCursor() {
    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    Box(
        modifier = Modifier
            .padding(start = 4.dp)
            .size(width = 8.dp, height = 18.dp)
            .background(AccentViolet.copy(alpha = alpha))
    )
}

@Composable
fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    isLoading: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BgDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceDark)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (text.isEmpty()) {
                    Text("Escribe un mensaje...", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                }
                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                    cursorBrush = SolidColor(AccentViolet),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { if (text.isNotBlank()) onSend() }
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(
                onClick = onSend,
                enabled = text.isNotBlank() && !isLoading,
                modifier = Modifier
                    .size(44.dp)
                    .background(if (text.isNotBlank()) AccentViolet else SurfaceVariant, CircleShape)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
