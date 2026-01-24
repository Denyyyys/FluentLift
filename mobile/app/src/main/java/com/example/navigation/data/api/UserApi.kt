package com.example.navigation.data.api

import com.example.navigation.data.dto.response.AppUserResponseDto
import retrofit2.http.GET

interface UserApi {
    @GET("api/v1/auth/userInfo")
    suspend fun getUserInfo(): AppUserResponseDto
}