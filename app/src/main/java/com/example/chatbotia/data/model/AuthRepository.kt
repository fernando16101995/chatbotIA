package com.example.chatbotia.data.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.BufferedReader

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

    suspend fun sendMessage(token: String, message: String): Result<ChatResponse> {
        return try {
            val authHeader = "Bearer $token"
            val response = api.sendMessage(authHeader, message)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistory(token: String): Result<List<HistoryItem>> {
        return try {
            val response = api.getHistory("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener historial"))
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

    /**
     * Procesa la respuesta de stream línea por línea manteniendo espacios.
     */
    fun streamMessage(token: String, message: String): Flow<String> = flow {
        try {
            val response = api.streamMessage("Bearer $token", StreamRequest(message))
            if (response.isSuccessful && response.body() != null) {
                val reader = response.body()!!.byteStream().bufferedReader()
                
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val currentLine = line ?: ""
                    if (currentLine.startsWith("data: ")) {
                        val data = currentLine.substring(6) // Extraemos después de "data: "
                        
                        if (data.trim() == "[DONE]") break
                        
                        // Emitimos el contenido tal cual viene (importante para espacios)
                        emit(data) 
                    }
                }
            } else {
                emit("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            emit("Error de conexión")
        }
    }.flowOn(Dispatchers.IO)

    // --- NUEVAS FUNCIONES DE ANÁLISIS ---

    suspend fun analyzePhq9(token: String, narrative: String): Result<Phq9Response> {
        return try {
            val response = api.analyzePhq9("Bearer $token", Phq9Request(narrative))
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
}
