package com.example.chatbotia.interfaz

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatbotia.data.model.AuthRepository
import com.example.chatbotia.data.model.ChatRepository
import com.example.chatbotia.data.model.TokenManager
import com.example.chatbotia.interfaz.chat.ChatViewModel
import com.example.chatbotia.interfaz.login.LoginViewModel
import com.example.chatbotia.interfaz.profile.ProfileViewModel
import com.example.chatbotia.interfaz.register.RegisterViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val tokenManager = TokenManager(context)
        val chatRepository = ChatRepository()
        
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(AuthRepository(), tokenManager) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(AuthRepository()) as T
            }
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel(chatRepository, tokenManager) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(chatRepository, tokenManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}