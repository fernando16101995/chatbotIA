package com.example.chatbotia.interfaz.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotia.data.model.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    fun register(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Completa todos los campos"
            return
        }

        if (password != confirmPassword) {
            errorMessage = "Las contraseñas no coinciden"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.register(email, password)
            isLoading = false

            result.onSuccess {
                successMessage = "Registro exitoso"
                onSuccess()
            }.onFailure { exception ->
                errorMessage = exception.message ?: "Error al registrarse"
            }
        }
    }
}