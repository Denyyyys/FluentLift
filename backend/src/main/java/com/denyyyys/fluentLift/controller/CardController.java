package com.denyyyys.fluentLift.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denyyyys.fluentLift.model.postgres.dto.request.CardProgressUpdateRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CardOwnerResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.Card;
import com.denyyyys.fluentLift.model.postgres.mapper.CardMapper;
import com.denyyyys.fluentLift.service.DeckService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final DeckService deckService;

    @PutMapping("/{cardId}/progress")
    public ResponseEntity<CardOwnerResponseDto> getDecks(@PathVariable Long cardId,
            @RequestBody CardProgressUpdateRequestDto request,
            @AuthenticationPrincipal UserDetails user) {

        Card updatedCard = deckService.updateCardProgress(cardId, request.isKnewIt(), user.getUsername());
        return ResponseEntity.ok().body(CardMapper.toCardOwnerResponseDto(updatedCard));
    }

}
