package com.example.navigation.data.repository

import com.example.navigation.data.RetrofitClient
import com.example.navigation.data.api.UserApi
import com.example.navigation.data.dto.response.AppUserResponseDto

class UserRepository(
    private val api: UserApi = RetrofitClient.userApi
) {
    suspend fun getUserInfo(): AppUserResponseDto {
        return api.getUserInfo()
    }
}