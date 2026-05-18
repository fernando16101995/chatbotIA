package com.example.chatbotia.interfaz.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotia.data.model.*
import com.example.chatbotia.data.model.ChatbotApiService
import kotlinx.coroutines.launch

class DashboardViewModel(private val apiService: ChatbotApiService) : ViewModel() {

    private val _dashboardMetrics = MutableLiveData<DashboardMetrics?>()
    val dashboardMetrics: LiveData<DashboardMetrics?> = _dashboardMetrics

    private val _usersList = MutableLiveData<UsersList?>()
    val usersList: LiveData<UsersList?> = _usersList

    private val _highRiskUsers = MutableLiveData<HighRiskUsers?>()
    val highRiskUsers: LiveData<HighRiskUsers?> = _highRiskUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadDashboardMetrics(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getDashboardMetrics("Bearer $token")
                if (response.isSuccessful) {
                    _dashboardMetrics.value = response.body()
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUsersList(token: String, limit: Int = 50, offset: Int = 0) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getUsersList("Bearer $token", limit, offset)
                if (response.isSuccessful) {
                    _usersList.value = response.body()
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadHighRiskUsers(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getHighRiskUsers("Bearer $token")
                if (response.isSuccessful) {
                    _highRiskUsers.value = response.body()
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _operationResult = MutableLiveData<String>()
    val operationResult: LiveData<String> = _operationResult

    fun deleteUser(token: String, userId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.deleteUser("Bearer $token", userId)
                if (response.isSuccessful) {
                    _operationResult.value = response.body()?.message ?: "Usuario eliminado"
                    onSuccess()
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(token: String, userId: Int, request: UpdateUserRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.updateUser("Bearer $token", userId, request)
                if (response.isSuccessful) {
                    _operationResult.value = response.body()?.message ?: "Usuario actualizado"
                    onSuccess()
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}