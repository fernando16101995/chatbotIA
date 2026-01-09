package com.example.chatbotia.interfaz.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotia.data.model.AuthRepository
import com.example.chatbotia.data.model.TokenManager
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository(),
    private val tokenManager: TokenManager
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun login(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Completa todos los campos"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.login(email, password)
            isLoading = false
            
            result.onSuccess { response ->
                tokenManager.saveToken(response.access_token)
                onSuccess()
            }.onFailure { exception ->
                errorMessage = exception.message ?: "Error al iniciar sesión"
            }
        }
    }
}