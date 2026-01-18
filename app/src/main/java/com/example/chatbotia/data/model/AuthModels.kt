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

// --- CHAT Y STREAM ---
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

// --- MODELOS DE ANÁLISIS (Sincronizados con el Servidor) ---

data class Phq9Request(
    val narrative: String
)

data class Phq9Response(
    @SerializedName("total_score") val totalScore: Int,
    val severity: String,
    val confidence: Double,
    val interpretation: String? = null
)

data class HealthSummary(
    @SerializedName("total_phq9_assessments") val totalAssessments: Int,
    @SerializedName("depression_detection_count") val depressionDetections: Int,
    @SerializedName("overall_risk_level") val currentRisk: String?,
    @SerializedName("requires_attention") val requiresAttention: Boolean,
    @SerializedName("latest_phq9_severity") val lastSeverity: String? = null
)

data class RiskAlert(
    @SerializedName("needs_attention") val needsAttention: Boolean,
    val reason: String?,
    val recommendation: String?
)

// --- EVALUACIÓN CONVERSACIONAL PHQ-9 ---

data class Phq9ConversationalStatus(
    val has_active_assessment: Boolean,
    val current_question: Int? = null,
    val completed_questions: Int? = null,
    val total_questions: Int? = null,
    val progress_percentage: Double? = null,
    val messages_since_last_question: Int? = null,
    val started_at: String? = null,
    val message: String? = null
)

data class Phq9ConversationalResult(
    val id: Int,
    val total_score: Int,
    val severity: String,
    val started_at: String,
    val completed_at: String,
    val responses: Map<String, QuestionResponse>
)

data class QuestionResponse(
    val response: String,
    val score: Int
)

data class MessageResponse(
    val message: String
)
