package com.example.chatbotia.data.model

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ChatbotApiService {
//--------------------Authetication--------------------//
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>
    @GET("auth/me")
    suspend fun getCurrentUserInfo(
        @Header("Authorization") token: String
    ): Response<CurrentUserInfo>

    //--------------------Chat-----------------------------//
    @Streaming
    @POST("chat/stream")
    suspend fun streamMessage(
        @Header("Authorization") token: String,
        @Body request: StreamRequest
    ): Response<ResponseBody>

    @GET("chat/history")
    suspend fun getHistory(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 100
    ): Response<ChatHistoryResponse>

    @DELETE("chat/history")
    suspend fun deleteHistory(
        @Header("Authorization") token: String
    ): Response<ResponseBody>

    @POST("chat/analyze-phq9")
    suspend fun analyzePhq9(
        @Header("Authorization") token: String,
        @Query("narrative") narrative: String
    ): Response<Phq9Response>

//--------------------Assessment---------------------//

    @POST("assessment/phq9/analyze")
    suspend fun analyzePhq9(
        @Header("Authorization") token: String,
        @Body request: Phq9Request
    ): Response<Phq9Response>

    @GET("assessment/summary")
    suspend fun getAssessmentSummary(
        @Header("Authorization") token: String
    ): Response<HealthSummary>

    @GET("assessment/phq9/history")
    suspend fun getPhq9History(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 10
    ): Response<List<Phq9Result>>

    @GET("assessment/phq9/latest")
    suspend fun getLatestPhq9(
        @Header("Authorization") token: String
    ): Response<Phq9Result>

    @GET("assessment/detections")
    suspend fun getDetections(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 20,
        @Query("only_positive") onlyPositive: Boolean = false
    ): Response<List<DetectionItem>>

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


    //------------metrics-------------------------------------------//

    // Dashboard Admin
    @GET("admin/dashboard/metrics")
    suspend fun getDashboardMetrics(
        @Header("Authorization") token: String
    ): Response<DashboardMetrics>

    @GET("admin/dashboard/users")
    suspend fun getUsersList(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): Response<UsersList>

    @GET("admin/dashboard/user/{user_id}")
    suspend fun getUserDetail(
        @Header("Authorization") token: String,
        @Path("user_id") userId: Int
    ): Response<UserDetail>

    @GET("admin/dashboard/high-risk-users")
    suspend fun getHighRiskUsers(
        @Header("Authorization") token: String
    ): Response<HighRiskUsers>
}