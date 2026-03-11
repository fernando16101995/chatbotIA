package com.example.chatbotia.data.model

import com.google.gson.annotations.SerializedName

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

data class StreamRequest(
    val message: String,
    @SerializedName("use_context") val useContext: Boolean = true
)

data class ChatHistoryResponse(
    val messages: List<HistoryMessage>,
    val total: Int
)

data class HistoryMessage(
    val role: String,
    val content: String,
    val created_at: String
)

data class Phq9Response(
    @SerializedName("assessment_id") val assessmentId: Int? = null,
    @SerializedName("total_score") val totalScore: Int,
    val severity: String,
    val symptoms: List<Phq9Symptom>? = null,
    val error: String? = null
)

data class Phq9Symptom(
    val numero: Int,
    val presente: Boolean,
    val confianza: Int
)

data class Phq9Result(
    val id: Int,
    @SerializedName("total_score") val totalScore: Int,
    val severity: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("q1_interest") val q1Interest: Int,
    @SerializedName("q2_depressed") val q2Depressed: Int,
    @SerializedName("q3_sleep") val q3Sleep: Int,
    @SerializedName("q4_energy") val q4Energy: Int,
    @SerializedName("q5_appetite") val q5Appetite: Int,
    @SerializedName("q6_failure") val q6Failure: Int,
    @SerializedName("q7_concentration") val q7Concentration: Int,
    @SerializedName("q8_movement") val q8Movement: Int,
    @SerializedName("q9_suicide") val q9Suicide: Int
)

data class DetectionItem(
    val id: Int,
    @SerializedName("is_depressive") val isDepressive: Boolean,
    @SerializedName("confidence_score") val confidenceScore: Double,
    @SerializedName("risk_level") val riskLevel: String,
    @SerializedName("detected_keywords") val detectedKeywords: List<String>?,
    @SerializedName("detected_at") val detectedAt: String
)

data class HealthSummary(
    @SerializedName("latest_phq9_score") val latestPhq9Score: Int?,
    @SerializedName("latest_phq9_severity") val latestPhq9Severity: String?,
    @SerializedName("latest_phq9_date") val latestPhq9Date: String?,
    @SerializedName("total_phq9_assessments") val totalPhq9Assessments: Int,
    @SerializedName("depression_detection_count") val depressionDetectionCount: Int,
    @SerializedName("last_detection_date") val lastDetectionDate: String?,
    @SerializedName("high_risk_detections") val highRiskDetections: Int,
    @SerializedName("overall_risk_level") val overallRiskLevel: String,
    @SerializedName("requires_attention") val requiresAttention: Boolean
)

data class RiskAlert(
    @SerializedName("requires_attention") val requiresAttention: Boolean,
    @SerializedName("risk_level") val riskLevel: String,
    @SerializedName("phq9_score") val phq9Score: Int?,
    @SerializedName("high_risk_detections") val highRiskDetections: Int,
    val message: String
)

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

data class QuestionResponse(
    val response: String?,
    val score: Int
)

data class Phq9ConversationalResult(
    val id: Int,
    val total_score: Int,
    val severity: String,
    val started_at: String,
    val completed_at: String?,
    val responses: Map<String, QuestionResponse>
)

data class MessageResponse(
    val message: String
)