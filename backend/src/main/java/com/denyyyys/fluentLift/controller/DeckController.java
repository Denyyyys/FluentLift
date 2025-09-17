package com.denyyyys.fluentLift.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.denyyyys.fluentLift.model.postgres.dto.CardCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.CardUpdateDto;
import com.denyyyys.fluentLift.model.postgres.dto.DeckCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.DeckUpdateDto;
import com.denyyyys.fluentLift.model.postgres.entity.Card;
import com.denyyyys.fluentLift.model.postgres.entity.Deck;
import com.denyyyys.fluentLift.service.DeckService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/decks")
@RequiredArgsConstructor
public class DeckController {
    private final DeckService deckService;
    // private

    @GetMapping
    public ResponseEntity<List<?>> getDecks(@AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) String creatorEmail) {
        if (creatorEmail == null || creatorEmail.isEmpty() || user.getUsername().equals(creatorEmail)) {
            return ResponseEntity.ok().body(deckService.getDecksDtoOwnedBy(user.getUsername()));
        }

        return ResponseEntity.ok().body(deckService.getDecksDtoVisibleToPublic(creatorEmail));
    }

    @GetMapping("/{deckId}")
    public ResponseEntity<Deck> getDeckById(@AuthenticationPrincipal UserDetails user, @PathVariable Long deckId) {
        Deck deck = deckService.getReadableDeck(deckId, user.getUsername());
        return ResponseEntity.ok().body(deck);
    }

    @PostMapping
    public ResponseEntity<Deck> createDeck(@AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody DeckCreateDto deckDto) {
        Deck newDeck = deckService.createDeck(deckDto, user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(newDeck);
    }

    @PutMapping("/{deckId}")
    public ResponseEntity<Deck> updateDeck(@AuthenticationPrincipal UserDetails user, @PathVariable Long deckId,
            @Valid @RequestBody DeckUpdateDto deckDto) {

        Deck updatedDeck = deckService.updateDeck(deckDto, deckId, user.getUsername());
        return ResponseEntity.ok().body(updatedDeck);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<String> deleteDeck(@AuthenticationPrincipal UserDetails user, @PathVariable Long deckId) {
        deckService.deleteDeck(deckId, user.getUsername());
        return ResponseEntity.ok().body("Deck successfully deleted");
    }

    @PostMapping("/{deckId}/cards")
    public ResponseEntity<Card> addCard(@AuthenticationPrincipal UserDetails user, @PathVariable Long deckId,
            @Valid @RequestBody CardCreateDto cardDto) {

        Card cardCreated = deckService.addCardToDeck(deckId, cardDto, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(cardCreated);
    }

    @PutMapping("/cards/{cardId}")
    public ResponseEntity<Card> updateCard(@AuthenticationPrincipal UserDetails user, @PathVariable Long cardId,
            @Valid @RequestBody CardUpdateDto card) {
        Card updated = deckService.updateCard(cardId, card, user.getUsername());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@AuthenticationPrincipal UserDetails user, @PathVariable Long cardId) {
        deckService.deleteCard(cardId, user.getUsername());
        return ResponseEntity.noContent().build();
    }

}
