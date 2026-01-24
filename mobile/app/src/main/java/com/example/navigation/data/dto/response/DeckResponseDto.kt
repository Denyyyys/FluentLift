package com.example.navigation.data.dto.response

import com.google.gson.annotations.SerializedName

data class DecksResponseDto(
    val decks: List<DeckDto>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)

data class DeckDto(
    val id: Long,
    val name: String,
    val creator: CreatorDto,
    val archived: Boolean,
    val targetLanguage: String,
    val baseLanguage: String,
    val cards: List<CardDto>,
    @SerializedName("public") val isPublic: Boolean
)

data class CreatorDto(
    val name: String,
    val email: String
)

data class CardDto(
    val id: Long,
    val frontText: String,
    val backText: String,
    val nextReviewDate: String?,
    val firstReviewDate: String?,
    val intervalDays: Int,
    val consecutiveCorrect: Int,
    val archived: Boolean
)

