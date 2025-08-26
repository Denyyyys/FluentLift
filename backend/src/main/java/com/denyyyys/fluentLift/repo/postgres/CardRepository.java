package com.denyyyys.fluentLift.repo.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.denyyyys.fluentLift.model.postgres.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByDeckId(Long deckId);
}
