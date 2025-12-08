package com.denyyyys.fluentLift.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denyyyys.fluentLift.exceptions.DuplicateResourceException;
import com.denyyyys.fluentLift.exceptions.ResourceNotFound;
import com.denyyyys.fluentLift.model.postgres.dto.CardCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.CardUpdateDto;
import com.denyyyys.fluentLift.model.postgres.dto.DeckCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CardOwnerResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CardVisitorResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckCreatorDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckOwnerResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckVisitorResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.Card;
import com.denyyyys.fluentLift.model.postgres.entity.Deck;
import com.denyyyys.fluentLift.model.postgres.mapper.AppUserMapper;
import com.denyyyys.fluentLift.model.postgres.mapper.CardMapper;
import com.denyyyys.fluentLift.model.postgres.mapper.DeckMapper;
import com.denyyyys.fluentLift.repo.postgres.AppUserRepository;
import com.denyyyys.fluentLift.repo.postgres.CardRepository;
import com.denyyyys.fluentLift.repo.postgres.DeckRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final AppUserRepository appUserRepository;

    @Transactional
    public Deck createDeck(DeckCreateDto dto, String creatorEmail) {
        AppUser creator = appUserRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new ResourceNotFound("Creator not found"));

        Deck deck = DeckMapper.toEntity(dto, creator);

        List<CardCreateDto> cardDtos = dto.getCards();
        if (cardDtos != null && !cardDtos.isEmpty()) {
            List<Card> cards = new ArrayList<>();
            for (CardCreateDto cardDto : cardDtos) {
                cards.add(CardMapper.toEntity(cardDto, deck));
            }

            deck.setCards(cards);
        }
        try {
            deckRepository.save(deck);
        } catch (DataIntegrityViolationException ex) {
            Throwable rootCause = ex.getRootCause();
            if (rootCause != null && rootCause.getMessage() != null
                    && rootCause.getMessage().contains("uk_deck_creatorid_name")) {
                throw new DuplicateResourceException("Deck with provided name already exists");
            }
            throw ex;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return deck;
    }

    @Transactional(readOnly = true)
    public Deck getReadableDeck(Long deckId, String userEmail) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ResourceNotFound("Deck with provided id was not found"));

        if (deck.isPublic() || deck.getCreator().getEmail().equals(userEmail)) {
            return deck;
        }

        throw new AccessDeniedException("This deck is private, you don't have access to it");
    }

    private Deck getWritableDeck(Long deckId, String userEmail) {
        return getWritableDeck(deckId, userEmail, "You do not have permission to modify this deck");
    }

    private Deck getWritableDeck(Long deckId, String userEmail, String accessDeniedMessage) {
        Deck deck = getReadableDeck(deckId, userEmail);
        if (!deck.getCreator().getEmail().equals(userEmail)) {
            throw new AccessDeniedException(accessDeniedMessage);
        }

        return deck;
    }

    @Transactional(readOnly = true)
    public List<DeckOwnerResponseDto> getDecksDtoOwnedBy(String ownerEmail) {
        List<Deck> decks = deckRepository.findAllByCreatorEmail(ownerEmail);
        List<DeckOwnerResponseDto> decksDto = decks.stream().map(deck -> {
            List<CardOwnerResponseDto> cardsDto = deck.getCards().stream()
                    .map(card -> CardMapper.toCardOwnerResponseDto(card))
                    .toList();
            DeckCreatorDto creatorDto = AppUserMapper.toDeckCreatorDto(deck.getCreator());
            return DeckMapper.toDeckOwnerResponseDto(deck, cardsDto, creatorDto);
        }).toList();
        return decksDto;
    }

    @Transactional(readOnly = true)
    public List<DeckVisitorResponseDto> getDecksDtoVisibleToPublic(String ownerEmail) {
        List<Deck> decks = deckRepository.findAllByCreatorEmail(ownerEmail);

        List<DeckVisitorResponseDto> decksDto = decks.stream().map(deck -> {
            List<CardVisitorResponseDto> cardsDto = deck.getCards().stream()
                    .map(card -> CardMapper.toCardVisitorResponseDto(card))
                    .toList();
            DeckCreatorDto creatorDto = AppUserMapper.toDeckCreatorDto(deck.getCreator());
            return DeckMapper.toDeckVisitorResponseDto(deck, cardsDto, creatorDto);
        }).toList();

        return decksDto.stream().filter((deck) -> deck.isPublic()).toList();
    }

    // @Transactional
    // public Deck updateDeck(DeckUpdateDto deckDto, Long deckId, String userEmail)
    // {
    // Deck deck = this.getWritableDeck(deckId, userEmail);

    // DeckMapper.updateEntityFromDto(deckDto, deck);

    // try {
    // deck.setUpdatedAt(Instant.now());
    // return deckRepository.save(deck);
    // } catch (DataIntegrityViolationException ex) {
    // Throwable rootCause = ex.getRootCause();
    // if (rootCause != null && rootCause.getMessage() != null
    // && rootCause.getMessage().contains("uk_deck_creatorid_name")) {
    // throw new DuplicateResourceException("You already have deck with that name.
    // Please rename it");
    // }
    // throw ex;
    // }
    // }

    @Transactional
    public Deck updateDeck(Deck deck, Long deckId, String userEmail) {
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        try {
            deck.setUpdatedAt(Instant.now());
            return deckRepository.save(deck);
        } catch (DataIntegrityViolationException ex) {
            Throwable rootCause = ex.getRootCause();
            if (rootCause != null && rootCause.getMessage() != null
                    && rootCause.getMessage().contains("uk_deck_creatorid_name")) {
                throw new DuplicateResourceException("You already have deck with that name. Please rename it");
            }
            throw ex;
        }
    }

    @Transactional
    public void deleteDeck(Long deckId, String userEmail) {
        Deck deck = this.getWritableDeck(deckId, userEmail);
        deckRepository.deleteById(deck.getId());
        return;
    }

    // Card related
    @Transactional
    public Card addCardToDeck(Long deckId, CardCreateDto cardDto, String userEmail) {
        Deck deck = this.getReadableDeck(deckId, userEmail);
        if (deck.getCreator().getEmail().equals(userEmail)) {
            Card savedCard = CardMapper.toEntity(cardDto, deck);
            deck.setUpdatedAt(Instant.now());
            deckRepository.save(deck);
            return cardRepository.save(savedCard);
        }
        throw new AccessDeniedException("You do not have permission to add card to this deck");
    }

    @Transactional
    public Card updateCard(Long cardId, CardUpdateDto cardDto, String userEmail) {
        Card card = this.findCardById(cardId);

        if (card.getDeck().getCreator().getEmail().equals(userEmail)) {
            CardMapper.updateEntityFromDto(cardDto, card);
            Card savedCard = cardRepository.save(card);

            Deck deck = savedCard.getDeck();
            deck.setUpdatedAt(Instant.now());
            deckRepository.save(deck);
            return savedCard;
        }
        throw new AccessDeniedException("You do not have permission to update card for this deck");
    }

    @Transactional
    public void deleteCard(Long cardId, String userEmail) {
        Card card = this.findCardById(cardId);
        if (card.getDeck().getCreator().getEmail().equals(userEmail)) {
            Deck deck = card.getDeck();
            cardRepository.delete(card);

            deck.setUpdatedAt(Instant.now());
            deckRepository.save(deck);
            return;
        }
        throw new AccessDeniedException("You do not have permission to delete card to this deck");
    }

    @Transactional
    public Card updateCardProgress(Long cardId, boolean knewIt, String userEmail) {
        Card card = this.findCardById(cardId);
        if (!card.getDeck().getCreator().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have permission to update card for this deck");
        }

        if (card.getFirstReviewDate() == null) {
            card.setFirstReviewDate(Instant.now());
        }

        if (knewIt) {
            card.setConsecutiveCorrect(card.getConsecutiveCorrect() + 1);
            card.setIntervalDays(Math.max(1, card.getIntervalDays() * 2));
        } else {
            card.setConsecutiveCorrect(0);
            card.setIntervalDays(1);
        }

        card.setNextReviewDate(Instant.now().plus(card.getIntervalDays(), ChronoUnit.DAYS));

        return cardRepository.save(card);
    }

    public Card findCardById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFound("Card not found"));
    }

}
