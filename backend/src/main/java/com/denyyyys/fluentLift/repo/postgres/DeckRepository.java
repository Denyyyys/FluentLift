package com.denyyyys.fluentLift.repo.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.denyyyys.fluentLift.model.postgres.entity.Deck;

public interface DeckRepository extends JpaRepository<Deck, Long> {

    List<Deck> findByCreatorEmail(String email);
}
