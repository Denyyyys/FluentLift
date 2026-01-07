package com.denyyyys.fluentLift.repo.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.denyyyys.fluentLift.model.neo4j.entity.polish.PolishPerfectiveVerbNode;

public interface PolishPerfectiveVerbRepository extends Neo4jRepository<PolishPerfectiveVerbNode, String> {
    @Query("""
            MATCH (v:PolishPerfectiveVerb {lemma: $lemma}),
                  (l:LanguageData {lemma: $lemma, language: 'PL'})
            MERGE (v)-[:CONJUGATION_OF]->(l)
            """)
    void linkPerfectiveVerbToLanguageData(String lemma);
}
