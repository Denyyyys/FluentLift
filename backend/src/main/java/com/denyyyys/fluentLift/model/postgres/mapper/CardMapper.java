package com.denyyyys.fluentLift.model.postgres.mapper;

import com.denyyyys.fluentLift.model.postgres.dto.CardCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.CardUpdateDto;
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
}
