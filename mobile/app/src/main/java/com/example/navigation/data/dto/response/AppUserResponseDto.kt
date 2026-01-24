package com.example.navigation.data.dto.response

import com.google.gson.annotations.SerializedName

data class AppUserResponseDto(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: Long
)