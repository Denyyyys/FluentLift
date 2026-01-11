package com.denyyyys.fluentLift.repo.postgres;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.denyyyys.fluentLift.model.postgres.entity.Deck;

public interface DeckRepository extends JpaRepository<Deck, Long> {

    @Query("""
            SELECT d FROM Deck d
            WHERE d.creator.email = :ownerEmail
            """)
    Page<Deck> findAllByCreatorEmail(
            @Param("ownerEmail") String ownerEmail,
            Pageable pageable);

    @Query("""
               SELECT d FROM Deck d
               WHERE (d.isArchived = false)
                AND (d.isPublic = true)
                AND (:baseLanguage IS NULL OR d.baseLanguage = :baseLanguage)
                AND (:targetLanguage IS NULL OR d.targetLanguage = :targetLanguage)
                AND (:nameLike IS NULL OR d.name LIKE CONCAT('%', :nameLike, '%'))
            """)
    Page<Deck> searchPublicDecks(
            @Param("baseLanguage") String baseLanguage,
            @Param("targetLanguage") String targetLanguage,
            @Param("nameLike") String nameLike,
            Pageable pageable);
}
