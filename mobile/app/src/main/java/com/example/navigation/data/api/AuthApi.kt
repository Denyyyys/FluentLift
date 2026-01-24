package com.example.navigation.data.api

import com.example.navigation.data.dto.request.SignInRequest
import com.example.navigation.data.dto.request.SignUpRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/addNewUser")
    suspend fun signUp(@Body body: SignUpRequest): Response<ResponseBody>

    @POST("api/v1/auth/generateToken")
    suspend fun signIn(@Body body: SignInRequest): Response<ResponseBody>
}