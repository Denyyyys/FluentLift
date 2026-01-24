package com.example.navigation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.data.dto.response.AppUserResponseDto
import com.example.navigation.data.repository.UserRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    var user by mutableStateOf<AppUserResponseDto?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun clearUser() {
        user = null
    }

    fun loadUserInfo() {
        if (user != null) return
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                user = repository.getUserInfo()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to load user info"
            } finally {
                isLoading = false
            }
        }
    }
}
