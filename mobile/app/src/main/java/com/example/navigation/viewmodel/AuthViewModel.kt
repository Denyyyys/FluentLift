package com.example.navigation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation.data.repository.AuthRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.navigation.data.auth.TokenProvider

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var jwtToken by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage  by mutableStateOf<String?>(null)
        private set

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                jwtToken = repository.signUp(name, email, password)
                TokenProvider.token = jwtToken
            } catch(e: Exception) {
                errorMessage  = e.message ?: "something went wrong"
            } finally {
                isLoading = false
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                jwtToken = repository.signIn(email, password)

                TokenProvider.token = jwtToken
            } catch(e: Exception) {
                errorMessage  = e.message ?: "something went wrong"
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        TokenProvider.token = null
        jwtToken = null
    }
}