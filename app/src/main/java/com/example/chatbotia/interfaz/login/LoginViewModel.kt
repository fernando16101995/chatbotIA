package com.example.chatbotia.interfaz.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotia.data.model.AuthRepository
import com.example.chatbotia.data.model.TokenManager
import com.example.chatbotia.data.model.RetrofitClient
import kotlinx.coroutines.launch
import android.util.Log

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository(),
    private val tokenManager: TokenManager
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isAdmin by mutableStateOf(false)  // ← Nuevo

    fun login(onSuccess: (isAdmin: Boolean) -> Unit) {
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

                // 🔑 Verificar si es admin
                checkIfAdmin(response.access_token, onSuccess)

            }.onFailure { exception ->
                errorMessage = exception.message ?: "Error al iniciar sesión"
            }
        }
    }

    private fun checkIfAdmin(token: String, onSuccess: (isAdmin: Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.api
                val response = apiService.getCurrentUserInfo("Bearer $token")

                Log.d("AdminCheck", "Response code: ${response.code()}")
                Log.d("AdminCheck", "Response body: ${response.body()}")

                if (response.isSuccessful) {
                    val userInfo = response.body()
                    isAdmin = userInfo?.is_admin ?: false
                    Log.d("AdminCheck", "isAdmin: $isAdmin")
                    onSuccess(isAdmin)
                } else {
                    Log.e("AdminCheck", "Error: ${response.errorBody()?.string()}")  // ← Debug
                    isAdmin = false
                    onSuccess(false)
                }
            } catch (e: Exception) {
                Log.e("AdminCheck", "Exception: ${e.message}")  // ← Debug
                isAdmin = false
                onSuccess(false)
            }
        }
    }
}