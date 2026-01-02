package com.denyyyys.fluentLift.repo.neo4j;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.denyyyys.fluentLift.model.neo4j.entity.Synset;

public interface SynsetRepository extends Neo4jRepository<Synset, String> {
    // @Query("""
    // MATCH (s:Synset)-[:HAS_LANGUAGE]->(l:LanguageData)
    // WHERE l.lemma = $lemma
    // MATCH (s:Synset)-[:HAS_LANGUAGE]->(l:LanguageData)
    // RETURN s
    // LIMIT 1
    // """)
    // Optional<Synset> findByAnyLanguageLemma(String lemma);

    Optional<Synset> findFirstByLanguagesLemma(String lemma);

}
