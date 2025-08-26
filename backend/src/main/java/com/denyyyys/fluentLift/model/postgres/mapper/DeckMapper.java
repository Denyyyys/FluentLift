package com.denyyyys.fluentLift.model.postgres.mapper;

import java.util.List;

import com.denyyyys.fluentLift.model.postgres.dto.DeckCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.DeckUpdateDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CardOwnerResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CardVisitorResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckCreatorDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckOwnerResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckVisitorResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.Deck;

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

    public static DeckOwnerResponseDto toDeckOwnerResponseDto(Deck deckEntity, List<CardOwnerResponseDto> cardsDto,
            DeckCreatorDto creatorDto) {
        DeckOwnerResponseDto dto = new DeckOwnerResponseDto();

        dto.setId(deckEntity.getId());
        dto.setName(deckEntity.getName());
        dto.setCreator(creatorDto);
        dto.setArchived(deckEntity.isArchived());
        dto.setTargetLanguage(deckEntity.getTargetLanguage());
        dto.setLearningLanguage(deckEntity.getLearningLanguage());
        dto.setCards(cardsDto);
        dto.setPublic(deckEntity.isPublic());

        return dto;
    }

    public static DeckVisitorResponseDto toDeckVisitorResponseDto(Deck deckEntity,
            List<CardVisitorResponseDto> cardsDto,
            DeckCreatorDto creatorDto) {
        DeckVisitorResponseDto dto = new DeckVisitorResponseDto();

        dto.setId(deckEntity.getId());
        dto.setName(deckEntity.getName());
        dto.setCreator(creatorDto);
        dto.setArchived(deckEntity.isArchived());
        dto.setTargetLanguage(deckEntity.getTargetLanguage());
        dto.setLearningLanguage(deckEntity.getLearningLanguage());
        dto.setCards(cardsDto);
        dto.setPublic(deckEntity.isPublic());

        return dto;
    }
}
