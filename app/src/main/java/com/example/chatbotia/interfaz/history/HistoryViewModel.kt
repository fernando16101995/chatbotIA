package com.example.chatbotia.interfaz.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotia.data.model.ChatRepository
import com.example.chatbotia.data.model.HistoryMessage
import com.example.chatbotia.data.model.TokenManager
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: ChatRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    val messages = mutableStateListOf<HistoryMessage>()
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var deleteSuccess by mutableStateOf(false)

    init {
        loadHistory()
    }

    fun loadHistory() {
        val token = tokenManager.getToken() ?: return
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            repository.getHistory(token).onSuccess { response ->
                messages.clear()
                messages.addAll(response.messages.reversed())
            }.onFailure {
                errorMessage = it.message
            }
            isLoading = false
        }
    }

    fun deleteHistory(onSuccess: () -> Unit) {
        val token = tokenManager.getToken() ?: return
        viewModelScope.launch {
            if (repository.deleteHistory(token)) {
                messages.clear()
                onSuccess()
            }
        }
    }
}
