package com.denyyyys.fluentLift.repo.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.denyyyys.fluentLift.model.neo4j.entity.polish.PolishImperfectiveVerbNode;

public interface PolishImperfectiveVerbRepository extends Neo4jRepository<PolishImperfectiveVerbNode, String> {
    @Query("""
            MATCH (v:PolishImperfectiveVerb {lemma: $lemma}),
                  (l:LanguageData {lemma: $lemma, language: 'PL'})
            MERGE (v)-[:CONJUGATION_OF]->(l)
            """)
    void linkImperfectiveVerbToLanguageData(String lemma);
}
