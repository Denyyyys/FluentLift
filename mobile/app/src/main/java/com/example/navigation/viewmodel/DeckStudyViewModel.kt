package com.example.navigation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.navigation.data.RetrofitClient
import com.example.navigation.data.api.CardsApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.navigation.data.dto.request.CardProgressRequest
import com.example.navigation.data.dto.response.CardDto
import com.example.navigation.data.dto.response.DeckDto
import com.example.navigation.data.repository.CardRepository
import kotlinx.coroutines.launch

class DeckStudyViewModel(
    private val repository: CardRepository = CardRepository()
) : ViewModel() {

    var currentCardIndex by mutableStateOf(0)
        private set

    var isFlipped by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var deck by mutableStateOf<DeckDto?>(null)
        private set

    fun loadDeck(deck: DeckDto) {
        this.deck = deck
        currentCardIndex = 0
        isFlipped = false
    }

    val currentCard: CardDto?
        get() = deck?.cards?.getOrNull(currentCardIndex)

    fun flipCard() {
        isFlipped = !isFlipped
    }

    fun skipCard() {
        isFlipped = false
        if (deck != null && currentCardIndex < deck!!.cards.size - 1) {
            currentCardIndex++
        } else {
            // reached end
            currentCardIndex = 0
        }
    }

    fun submitProgress(knewIt: Boolean) {
        val card = currentCard ?: return
        viewModelScope.launch {
            try {
                repository.updateCardProgress(card.id, CardProgressRequest(knewIt))
                skipCard() // go to next card after submission
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to update progress"
            }
        }
    }
}
