package com.example.chatbotia.data.model

import android.content.Context

class TokenManager(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("chatbot_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun clearToken() {
        prefs.edit().remove("access_token").apply()
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}