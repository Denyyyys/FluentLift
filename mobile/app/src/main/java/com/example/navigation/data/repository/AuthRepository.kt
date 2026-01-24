package com.example.navigation.data.repository

import com.example.navigation.data.RetrofitClient
import com.example.navigation.data.api.AuthApi
import com.example.navigation.data.dto.request.SignInRequest
import com.example.navigation.data.dto.request.SignUpRequest

class AuthRepository(
    private val api: AuthApi = RetrofitClient.authApi
) {
    suspend fun signUp(name: String, email: String, password: String): String {
        var response =  api.signUp(SignUpRequest(name, email, password))
        if (response.isSuccessful) {
            return response.body()?.string() ?: throw Exception("empty response when signing up")
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Error ${response.code()}")
        }
    }

    suspend fun signIn(email: String, password: String): String {
        var response =  api.signIn(SignInRequest(email,password))
        if (response.isSuccessful) {
            return response.body()?.string() ?: throw Exception("empty response when signing in")
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Error ${response.code()}")
        }
    }
}