package com.example.navigation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.navigation.data.dto.response.CardDto
import com.example.navigation.ui.theme.Primary200
import com.example.navigation.ui.theme.Primary900
import com.example.navigation.viewmodel.AuthViewModel
import com.example.navigation.viewmodel.DeckStudyViewModel

@Composable
fun DeckStudyScreen(
    deckStudyViewModel: DeckStudyViewModel,
    onGoBack: () -> Unit
) {
    val card = deckStudyViewModel.currentCard
    val isFlipped = deckStudyViewModel.isFlipped
    val errorMessage = deckStudyViewModel.errorMessage
    val deckName = deckStudyViewModel.deck?.name.orEmpty()

    if (card == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = deckName,
            style = MaterialTheme.typography.titleLarge,
            color = Primary900,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Primary200)
                .padding(24.dp)
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isFlipped) card.backText else card.frontText,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }

            if (!isFlipped) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { deckStudyViewModel.flipCard() }) {
                        Text("Flip")
                    }
                    Button(onClick = { deckStudyViewModel.skipCard() }) {
                        Text("Skip")
                    }
                }
            } else {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = { deckStudyViewModel.submitProgress(true) }) {
                            Text("Knew It")
                        }
                        Button(onClick = { deckStudyViewModel.submitProgress(false) }) {
                            Text("Didn't Know It")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = { deckStudyViewModel.flipCard() }) {
                            Text("Flip")
                        }
                        Button(onClick = { deckStudyViewModel.skipCard() }) {
                            Text("Skip")
                        }
                    }
                }
            }
        }

        Button(
            onClick = onGoBack,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 60.dp)
                .fillMaxWidth(0.6f)
                .height(48.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Primary900)
        ) {
            Text("Go back to all decks", color = Color.White)
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )
        }
    }
}


