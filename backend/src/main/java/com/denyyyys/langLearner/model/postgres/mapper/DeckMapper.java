package com.denyyyys.langLearner.model.postgres.mapper;

import com.denyyyys.langLearner.model.postgres.dto.DeckCreateDto;
import com.denyyyys.langLearner.model.postgres.dto.DeckUpdateDto;
import com.denyyyys.langLearner.model.postgres.entity.AppUser;
import com.denyyyys.langLearner.model.postgres.entity.Deck;

public class DeckMapper {
    public static Deck toEntity(DeckCreateDto dto, AppUser creator) {
        Deck deck = new Deck();

        deck.setName(dto.getName());
        deck.setTargetLanguage(dto.getTargetLanguage());
        deck.setLearningLanguage(dto.getLearningLanguage());
        deck.setCreator(creator);

        return deck;
    }

    public static void updateEntityFromDto(DeckUpdateDto dto, Deck entity) {
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            entity.setName(dto.getName());
        }

        entity.setLearningLanguage(dto.getLearningLanguage());

        entity.setTargetLanguage(dto.getTargetLanguage());
    }
}
