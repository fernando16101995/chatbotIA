package com.example.chatbotia.interfaz.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatbotia.data.model.ChatbotApiService

class DashboardViewModelFactory(
    private val apiService: ChatbotApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}