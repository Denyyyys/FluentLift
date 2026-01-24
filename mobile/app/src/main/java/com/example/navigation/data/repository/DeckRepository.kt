package com.example.navigation.data.repository

import com.example.navigation.data.RetrofitClient
import com.example.navigation.data.api.DeckApi
import com.example.navigation.data.dto.response.DecksResponseDto

class DecksRepository(
    private val api: DeckApi = RetrofitClient.decksApi
) {
    suspend fun getMyDecks(): DecksResponseDto {
        return api.getMyDecks()
    }
}