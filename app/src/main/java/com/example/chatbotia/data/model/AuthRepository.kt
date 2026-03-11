package com.example.chatbotia.data.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository {
    private val api = RetrofitClient.api

    suspend fun register(email: String, password: String): Result<UserResponse> {
        return try {
            val response = api.register(RegisterRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<TokenResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class ChatRepository {
    private val api = RetrofitClient.api

    suspend fun getHistory(token: String): Result<ChatHistoryResponse> {
        return try {
            val response = api.getHistory("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener historial: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteHistory(token: String): Boolean {
        return try {
            val response = api.deleteHistory("Bearer $token")
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    fun streamMessage(token: String, message: String): Flow<String> = flow {
        try {
            val response = api.streamMessage(
                "Bearer $token",
                StreamRequest(message = message, useContext = true)
            )

            if (response.isSuccessful && response.body() != null) {
                val reader = response.body()!!.byteStream().bufferedReader()

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val currentLine = line.orEmpty()

                    if (currentLine.startsWith("data: ")) {
                        val data = currentLine.removePrefix("data: ")

                        if (data.trim() == "[DONE]") {
                            break
                        }

                        if (data.isNotBlank()) {
                            emit(data)
                        }
                    }
                }
            } else {
                emit("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            emit("Error de conexión")
        }
    }.flowOn(Dispatchers.IO)

    suspend fun analyzePhq9(token: String, narrative: String): Result<Phq9Response> {
        return try {
            val response = api.analyzePhq9("Bearer $token", narrative)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al analizar texto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAssessmentSummary(token: String): Result<HealthSummary> {
        return try {
            val response = api.getAssessmentSummary("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener resumen: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRiskAlert(token: String): Result<RiskAlert> {
        return try {
            val response = api.getRiskAlert("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener alerta: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPhq9History(token: String): Result<List<Phq9Result>> {
        return try {
            val response = api.getPhq9History("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener historial PHQ-9: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLatestPhq9(token: String): Result<Phq9Result> {
        return try {
            val response = api.getLatestPhq9("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener último PHQ-9: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetections(
        token: String,
        limit: Int = 20,
        onlyPositive: Boolean = false
    ): Result<List<DetectionItem>> {
        return try {
            val response = api.getDetections(
                token = "Bearer $token",
                limit = limit,
                onlyPositive = onlyPositive
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener detecciones: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPhq9ConversationalStatus(token: String): Result<Phq9ConversationalStatus> {
        return try {
            val response = api.getPhq9ConversationalStatus("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener estado PHQ-9 conversacional: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPhq9ConversationalHistory(
        token: String,
        limit: Int = 10
    ): Result<List<Phq9ConversationalResult>> {
        return try {
            val response = api.getPhq9ConversationalHistory("Bearer $token", limit)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener historial conversacional: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelPhq9Conversational(token: String): Result<MessageResponse> {
        return try {
            val response = api.cancelPhq9Conversational("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cancelar evaluación: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}