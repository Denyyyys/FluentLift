package com.example.navigation.data.api

import com.example.navigation.data.dto.response.DecksResponseDto
import retrofit2.http.GET

interface DeckApi {
    @GET("api/v1/decks/me")
    suspend fun getMyDecks(): DecksResponseDto
}