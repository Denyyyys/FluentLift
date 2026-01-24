package com.example.navigation.data.api

import com.example.navigation.data.dto.request.CardProgressRequest
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface CardsApi {
    @PUT("api/v1/cards/{cardId}/progress")
    suspend fun updateCardProgress(
        @Path("cardId") cardId: Long,
        @Body body: CardProgressRequest
    )
}