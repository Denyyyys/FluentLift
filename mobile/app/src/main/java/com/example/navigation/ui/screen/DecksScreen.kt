package com.example.navigation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navigation.ui.component.DeckCard
import com.example.navigation.ui.theme.Primary900
import com.example.navigation.viewmodel.AuthViewModel
import com.example.navigation.viewmodel.DecksViewModel

@Composable
fun DecksScreen(authViewModel: AuthViewModel, decksViewModel: DecksViewModel, onDeckClick: (deckId: Long) -> Unit) {
    val decks = decksViewModel.decks
    val isLoading = decksViewModel.isLoading
    val errorMessage = decksViewModel.errorMessage
    val primaryColor = Primary900

    LaunchedEffect(Unit) {
        if (
            decksViewModel.decks.size == 0
        ) {
            decksViewModel.loadDecks()
        }

    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (errorMessage != null) {
            Text(text = errorMessage, modifier = Modifier.align(Alignment.Center))
        } else {
            Text(text = "My Decks", color = primaryColor, style = MaterialTheme.typography.titleLarge, modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 80.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(decks) { deck ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,

                    ) {
                        DeckCard(
                            deck = deck,
                            primaryColor = primaryColor,
                            onClick = {
                                decksViewModel.selectedDeck = deck
                                onDeckClick(deck.id)
                            }
                        )
                    }
                }
            }
        }
    }
}