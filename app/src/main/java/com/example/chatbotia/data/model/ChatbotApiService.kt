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

    // --- NUEVOS ENDPOINTS DE ANÁLISIS Y SALUD ---

    @POST("chat/analyze-phq9")
    suspend fun analyzePhq9(
        @Header("Authorization") token: String,
        @Body request: Phq9Request
    ): Response<Phq9Response>

    @GET("assessment/summary")
    suspend fun getAssessmentSummary(
        @Header("Authorization") token: String
    ): Response<HealthSummary>

    @GET("assessment/risk-alert")
    suspend fun getRiskAlert(
        @Header("Authorization") token: String
    ): Response<RiskAlert>

    @GET("assessment/phq9/conversational/status")
    suspend fun getPhq9ConversationalStatus(
        @Header("Authorization") token: String
    ): Response<Phq9ConversationalStatus>

    @GET("assessment/phq9/conversational/history")
    suspend fun getPhq9ConversationalHistory(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 10
    ): Response<List<Phq9ConversationalResult>>

    @DELETE("assessment/phq9/conversational/cancel")
    suspend fun cancelPhq9Conversational(
        @Header("Authorization") token: String
    ): Response<MessageResponse>
}