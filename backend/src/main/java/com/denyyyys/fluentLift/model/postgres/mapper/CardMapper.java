package com.denyyyys.fluentLift.model.postgres.mapper;

import com.denyyyys.fluentLift.model.postgres.dto.CardCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.CardUpdateDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CardOwnerResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CardVisitorResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.Card;
import com.denyyyys.fluentLift.model.postgres.entity.Deck;

public class CardMapper {
    public static Card toEntity(CardCreateDto cardDto, Deck deck) {
        Card card = new Card();

        card.setBackText(cardDto.getBackText());
        card.setFrontText(cardDto.getFrontText());
        card.setDeck(deck);

        return card;
    }

    public static void updateEntityFromDto(CardUpdateDto dto, Card entity) {
        if (dto.getIsArchived() != null) {
            entity.setArchived(dto.getIsArchived());
        }

        if (dto.getBackText() != null) {
            entity.setBackText(dto.getBackText());
        }

        if (dto.getFrontText() != null) {
            entity.setFrontText(dto.getFrontText());
        }
    }

    public static CardOwnerResponseDto toCardOwnerResponseDto(Card entity) {
        CardOwnerResponseDto dto = new CardOwnerResponseDto();

        dto.setId(entity.getId());
        dto.setFrontText(entity.getFrontText());
        dto.setBackText(entity.getBackText());
        dto.setArchived(entity.isArchived());
        dto.setNextReviewDate(entity.getNextReviewDate());
        dto.setFirstReviewDate(entity.getFirstReviewDate());
        dto.setIntervalDays(entity.getIntervalDays());
        dto.setConsecutiveCorrect(entity.getConsecutiveCorrect());

        return dto;
    }

    public static CardVisitorResponseDto toCardVisitorResponseDto(Card entity) {
        CardVisitorResponseDto dto = new CardVisitorResponseDto();

        dto.setId(entity.getId());
        dto.setFrontText(entity.getFrontText());
        dto.setBackText(entity.getBackText());
        dto.setArchived(entity.isArchived());

        return dto;
    }
}
