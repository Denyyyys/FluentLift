package com.denyyyys.fluentLift.model.postgres.mapper;

import com.denyyyys.fluentLift.model.postgres.dto.DeckCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.DeckUpdateDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckOwnerResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckVisitorResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.Deck;

public class DeckMapper {
    public static Deck toEntity(DeckCreateDto dto, AppUser creator) {
        Deck deck = new Deck();

        deck.setName(dto.getName());
        deck.setTargetLanguage(dto.getTargetLanguage());
        deck.setBaseLanguage(dto.getBaseLanguage());
        deck.setCreator(creator);
        deck.setPublic(dto.getIsPublic());
        return deck;
    }

    public static void updateEntityFromDto(DeckUpdateDto dto, Deck entity) {
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            entity.setName(dto.getName());
        }

        entity.setBaseLanguage(dto.getBaseLanguage());

        entity.setTargetLanguage(dto.getTargetLanguage());
    }

    public static DeckOwnerResponseDto toDeckOwnerResponseDto(Deck deckEntity) {
        DeckOwnerResponseDto dto = new DeckOwnerResponseDto();

        dto.setId(deckEntity.getId());
        dto.setName(deckEntity.getName());
        dto.setCreator(AppUserMapper.toDeckCreatorDto(deckEntity.getCreator()));
        dto.setArchived(deckEntity.isArchived());
        dto.setTargetLanguage(deckEntity.getTargetLanguage());
        dto.setBaseLanguage(deckEntity.getBaseLanguage());
        dto.setCards(deckEntity.getCards().stream().map(CardMapper::toCardOwnerResponseDto).toList());
        dto.setPublic(deckEntity.isPublic());

        return dto;
    }

    public static DeckVisitorResponseDto toDeckVisitorResponseDto(Deck deckEntity) {
        DeckVisitorResponseDto dto = new DeckVisitorResponseDto();

        dto.setId(deckEntity.getId());
        dto.setName(deckEntity.getName());
        dto.setCreator(AppUserMapper.toDeckCreatorDto(deckEntity.getCreator()));
        dto.setArchived(deckEntity.isArchived());
        dto.setTargetLanguage(deckEntity.getTargetLanguage());
        dto.setBaseLanguage(deckEntity.getBaseLanguage());
        dto.setCards(deckEntity.getCards().stream().map(CardMapper::toCardVisitorResponseDto).toList());
        dto.setPublic(deckEntity.isPublic());

        return dto;
    }
}
