package com.example.chatbotia.data.model

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ChatbotApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    @POST("chat/")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Query("message") message: String
    ): Response<ChatResponse>

    // --- NUEVOS ENDPOINTS ---

    @Streaming
    @POST("chat/stream")
    suspend fun streamMessage(
        @Header("Authorization") token: String,
        @Body request: StreamRequest
    ): Response<ResponseBody>

    @GET("chat/history")
    suspend fun getHistory(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 50
    ): Response<List<HistoryItem>>

    @DELETE("chat/history")
    suspend fun deleteHistory(
        @Header("Authorization") token: String
    ): Response<ResponseBody>
}