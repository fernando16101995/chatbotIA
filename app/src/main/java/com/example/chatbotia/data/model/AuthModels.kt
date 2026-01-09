package com.example.chatbotia.data.model

import com.google.gson.annotations.SerializedName

// AuthModels.kt
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String
)

data class TokenResponse(
    val access_token: String,
    val token_type: String
)

data class UserResponse(
    val message: String,
    val email: String
)

data class ChatResponse(
    val reply: String
)

// --- NUEVOS MODELOS ---

data class StreamRequest(
    val message: String,
    @SerializedName("use_context") val useContext: Boolean = true
)

data class HistoryItem(
    val id: Int? = null,
    val message: String,
    val reply: String,
    val timestamp: String? = null
)
