package com.denyyyys.fluentLift.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denyyyys.fluentLift.config.Constants;
import com.denyyyys.fluentLift.exceptions.DuplicateResourceException;
import com.denyyyys.fluentLift.exceptions.ResourceNotFound;
import com.denyyyys.fluentLift.model.postgres.dto.CardCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.CardUpdateDto;
import com.denyyyys.fluentLift.model.postgres.dto.DeckCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckOwnerPageResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckOwnerResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckVisitorPageResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.DeckVisitorResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.Card;
import com.denyyyys.fluentLift.model.postgres.entity.Deck;
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

    @Transactional(transactionManager = "transactionManager")
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

    @Transactional(readOnly = true, transactionManager = "transactionManager")
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

    @Transactional(readOnly = true, transactionManager = "transactionManager")
    public DeckOwnerPageResponseDto getDecksDtoOwnedBy(String ownerEmail, int page, int size) {
        appUserRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Deck> deckPage = deckRepository.findAllByCreatorEmail(
                ownerEmail,
                pageable);

        List<DeckOwnerResponseDto> decksDto = deckPage.getContent()
                .stream()
                .map(DeckMapper::toDeckOwnerResponseDto)
                .toList();
        DeckOwnerPageResponseDto response = new DeckOwnerPageResponseDto();
        response.setDecks(decksDto);
        response.setPage(deckPage.getNumber() + 1);
        response.setSize(deckPage.getSize());
        response.setTotalElements(deckPage.getTotalElements());
        response.setTotalPages(deckPage.getTotalPages());

        return response;
    }

    @Transactional(readOnly = true, transactionManager = "transactionManager")
    public DeckVisitorPageResponseDto getDecksDtoVisibleToPublic(String baseLanguage,
            String targetLanguage, String nameLike, String sortBy, int page, int size, String userEmail) {

        appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        String sortField = switch (sortBy.toLowerCase()) {
            case "createdat" -> Constants.DEFAULT_SORT_DECKS_BY;
            case "name" -> Constants.SORT_DECKS_BY_NAME;
            default -> Constants.DEFAULT_SORT_QUESTIONS_BY;
        };

        size = Math.min(size, Constants.MAX_PAGE_SIZE);
        page = Math.max(page, 1);

        if (nameLike == null) {
            nameLike = "";
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortField).ascending());

        Page<Deck> deckPage = deckRepository.searchPublicDecks(
                baseLanguage, targetLanguage, nameLike,
                pageable);

        List<DeckVisitorResponseDto> decksDto = deckPage.getContent()
                .stream()
                .map(DeckMapper::toDeckVisitorResponseDto)
                .toList();

        DeckVisitorPageResponseDto response = new DeckVisitorPageResponseDto();
        response.setDecks(decksDto);
        response.setPage(deckPage.getNumber() + 1);
        response.setSize(deckPage.getSize());
        response.setTotalElements(deckPage.getTotalElements());
        response.setTotalPages(deckPage.getTotalPages());

        return response;
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

    @Transactional(transactionManager = "transactionManager")
    public Deck updateDeck(Deck deck, Long deckId, String userEmail) {
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        try {
            if (user.getId() != deck.getCreator().getId()) {
                throw new AccessDeniedException("Only creator of deck can modify it");
            }
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

    @Transactional(transactionManager = "transactionManager")
    public void deleteDeck(Long deckId, String userEmail) {
        Deck deck = this.getWritableDeck(deckId, userEmail);
        deckRepository.deleteById(deck.getId());
        return;
    }

    // Card related
    @Transactional(transactionManager = "transactionManager")
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

    @Transactional(transactionManager = "transactionManager")
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

    @Transactional(transactionManager = "transactionManager")
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

    @Transactional(transactionManager = "transactionManager")
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

    public DeckOwnerResponseDto copyDeck(Long deckId, String userEmail) {
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        Deck sourceDeck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ResourceNotFound("Deck with provided id was not found"));

        boolean isOwner = sourceDeck.getCreator().getEmail().equals(userEmail);
        boolean isPublicAndActive = sourceDeck.isPublic() && !sourceDeck.isArchived();

        if (!isOwner && !isPublicAndActive) {
            throw new AccessDeniedException(
                    "Cannot copy deck with id: " + sourceDeck.getId());
        }

        Deck copyDeck = new Deck();
        if (isOwner) {
            copyDeck.setName(sourceDeck.getName() + " copy");
        } else {
            copyDeck.setName(sourceDeck.getName());
        }
        copyDeck.setBaseLanguage(sourceDeck.getBaseLanguage());
        copyDeck.setTargetLanguage(sourceDeck.getTargetLanguage());
        copyDeck.setCreator(user);

        if (sourceDeck.getCards() != null) {
            List<Card> copiedCards = new ArrayList<>();
            for (Card c : sourceDeck.getCards()) {
                Card newCard = new Card();
                newCard.setFrontText(c.getFrontText());
                newCard.setBackText(c.getBackText());
                newCard.setDeck(copyDeck);

                copiedCards.add(newCard);
            }
            copyDeck.setCards(copiedCards);
        }

        copyDeck = deckRepository.save(copyDeck);

        return DeckMapper.toDeckOwnerResponseDto(copyDeck);
    }

}
