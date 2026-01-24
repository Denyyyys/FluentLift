package com.example.navigation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.navigation.data.repository.DecksRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.navigation.data.dto.response.DeckDto
import kotlinx.coroutines.launch

class DecksViewModel(
    private val repository: DecksRepository = DecksRepository()
) : ViewModel() {
    var decks by mutableStateOf<List<DeckDto>>(emptyList())
        private set

    var selectedDeck: DeckDto? = null

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set



    fun loadDecks() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                decks = repository.getMyDecks().decks
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to load decks"
            } finally {
                isLoading = false
            }
        }
    }
}