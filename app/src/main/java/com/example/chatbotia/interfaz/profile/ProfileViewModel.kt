package com.example.chatbotia.interfaz.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotia.data.model.ChatRepository
import com.example.chatbotia.data.model.HealthSummary
import com.example.chatbotia.data.model.TokenManager
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ChatRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    var summary by mutableStateOf<HealthSummary?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadSummary()
    }

    fun loadSummary() {
        val token = tokenManager.getToken() ?: return
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.getAssessmentSummary(token).onSuccess {
                summary = it
                isLoading = false
            }.onFailure {
                errorMessage = it.message ?: "Error al cargar el resumen"
                isLoading = false
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        tokenManager.clearToken()
        onLogout()
    }
}