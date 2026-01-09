package com.example.chatbotia.interfaz.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotia.data.model.ChatRepository
import com.example.chatbotia.data.model.TokenManager
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository(),
    private val tokenManager: TokenManager
) : ViewModel() {

    val messages = mutableStateListOf<ChatMessage>()

    var inputText by mutableStateOf("")
    var isTyping by mutableStateOf(false)

    init {
        loadHistory()
    }

    private fun loadHistory() {
        val token = tokenManager.getToken() ?: return
        viewModelScope.launch {
            repository.getHistory(token).onSuccess { history ->
                messages.clear()
                if (history.isEmpty()) {
                    messages.add(ChatMessage("¡Hola! Soy Seren 👋 ¿Cómo te sientes hoy?", false))
                } else {
                    history.forEach { item ->
                        messages.add(ChatMessage(item.message, true))
                        messages.add(ChatMessage(item.reply, false))
                    }
                }
            }
        }
    }

    fun sendMessage() {
        val messageText = inputText.trim()
        if (messageText.isBlank()) return

        val token = tokenManager.getToken()
        if (token == null) {
            messages.add(ChatMessage("Error: Sesión expirada.", false))
            return
        }

        // 1. Agregar mensaje del usuario a la lista
        messages.add(ChatMessage(messageText, true))
        inputText = ""
        isTyping = true

        // 2. Crear un mensaje vacío para el bot que iremos rellenando (Efecto Streaming)
        val botMessageIndex = messages.size
        messages.add(ChatMessage("", false))

        viewModelScope.launch {
            var fullReply = ""
            repository.streamMessage(token, messageText).collect { chunk ->
                isTyping = false
                fullReply += chunk
                // Actualizamos el último mensaje con el nuevo fragmento
                messages[botMessageIndex] = ChatMessage(fullReply, false)
            }
        }
    }

    fun clearChat() {
        val token = tokenManager.getToken() ?: return
        viewModelScope.launch {
            if (repository.deleteHistory(token)) {
                messages.clear()
                messages.add(ChatMessage("Historial borrado. ¡Hola de nuevo! 👋", false))
            }
        }
    }
}