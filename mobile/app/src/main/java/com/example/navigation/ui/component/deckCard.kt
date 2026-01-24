package com.example.navigation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.navigation.data.dto.response.DeckDto
import com.example.navigation.ui.theme.Primary200
import java.time.ZonedDateTime

@Composable
fun DeckCard(deck: DeckDto, primaryColor: Color, onClick: (deckId: Long) -> Unit) {
    val newCards = deck.cards.count { it.firstReviewDate == null }
    val toLearn = deck.cards.count {
        it.firstReviewDate == null || (it.nextReviewDate?.let { ZonedDateTime.parse(it).isBefore(ZonedDateTime.now()) } ?: false)
    }
    val total = deck.cards.size
        Column (
        modifier = Modifier
            .padding(8.dp)
            .widthIn(max = 350.dp)
            .background(Primary200)
            .clickable { onClick(deck.id) }
            .padding(16.dp)
    ) {
        Text(text = "Name: ${deck.name}", color = primaryColor, style = MaterialTheme.typography.titleMedium)
        Text(text = "Base Language: ${deck.baseLanguage}", color = primaryColor, style = MaterialTheme.typography.bodyMedium)
        Text(text = "Target Language: ${deck.targetLanguage}", color = primaryColor, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("New cards")
            Text("To learn")
            Text("Total")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("$newCards")
            Text("$toLearn")
            Text("$total")
        }

    }
}