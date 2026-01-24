package com.example.navigation.data.repository

import com.example.navigation.data.RetrofitClient
import com.example.navigation.data.api.CardsApi
import com.example.navigation.data.api.DeckApi
import com.example.navigation.data.dto.request.CardProgressRequest
import com.example.navigation.data.dto.response.DecksResponseDto

class CardRepository(
    private val api: CardsApi = RetrofitClient.cardsApi
) {
    suspend fun updateCardProgress(cardId: Long, cardProgressRequest: CardProgressRequest) {
        return api.updateCardProgress(cardId, cardProgressRequest)
    }
}