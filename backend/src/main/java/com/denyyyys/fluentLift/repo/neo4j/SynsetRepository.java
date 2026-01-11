package com.denyyyys.fluentLift.repo.neo4j;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.denyyyys.fluentLift.model.neo4j.entity.Synset;

public interface SynsetRepository extends Neo4jRepository<Synset, String> {
    // @Query("""
    // MATCH (s:Synset)-[:HAS_LANGUAGE]->(l:LanguageData)
    // WHERE l.lemma = $lemma
    // MATCH (s:Synset)-[:HAS_LANGUAGE]->(l:LanguageData)
    // RETURN s
    // LIMIT 1
    // """)
    Optional<Synset> findFirstByLanguagesLemma(String lemma);

    @Query("""
            MATCH (l:LanguageData)
            WHERE l.lemma = $word
            MATCH (l)<-[:HAS_LANGUAGE]-(s:Synset)
            OPTIONAL MATCH (s)-[:HAS_LANGUAGE]->(langs:LanguageData)
            RETURN s, collect(langs) as languages
            LIMIT 1

            UNION

            MATCH (v)
            WHERE v:PolishPerfectiveVerb OR v:PolishImperfectiveVerb
            AND (
                v.lemma = $word OR
                $word IN [
                    v.pres_sg_1, v.pres_sg_2, v.pres_sg_3,
                    v.pres_pl_1, v.pres_pl_2, v.pres_pl_3,
                    v.fut_sg_m_1, v.fut_sg_m_2, v.fut_sg_m_3,
                    v.fut_sg_f_1, v.fut_sg_f_2, v.fut_sg_f_3,
                    v.fut_sg_n_1, v.fut_sg_n_2, v.fut_sg_n_3,
                    v.fut_pl_mp_1, v.fut_pl_mp_2, v.fut_pl_mp_3,
                    v.fut_pl_nmp_1, v.fut_pl_nmp_2, v.fut_pl_nmp_3,
                    v.past_sg_m_1, v.past_sg_m_2, v.past_sg_m_3,
                    v.past_sg_f_1, v.past_sg_f_2, v.past_sg_f_3,
                    v.past_sg_n_1, v.past_sg_n_2, v.past_sg_n_3,
                    v.past_pl_mp_1, v.past_pl_mp_2, v.past_pl_mp_3,
                    v.past_pl_nmp_1, v.past_pl_nmp_2, v.past_pl_nmp_3,
                    v.cond_sg_m_1, v.cond_sg_m_2, v.cond_sg_m_3,
                    v.cond_sg_f_1, v.cond_sg_f_2, v.cond_sg_f_3,
                    v.cond_sg_n_1, v.cond_sg_n_2, v.cond_sg_n_3,
                    v.cond_pl_mp_1, v.cond_pl_mp_3, v.cond_pl_mp_3,
                    v.cond_pl_nmp_1, v.cond_pl_nmp_3, v.cond_pl_nmp_3,
                    v.imp_sg_2, v.imp_pl_1, v.imp_pl_2,
                    v.activeAdjectivalParticiple, v.passiveAdjectivalParticiple,
                    v.contemporaryAdverbialParticiple, v.gerund
                ]
            )
            MATCH (v)-[:CONJUGATION_OF]->(l:LanguageData)
            MATCH (l)<-[:HAS_LANGUAGE]-(s:Synset)
            OPTIONAL MATCH (s)-[r:HAS_LANGUAGE]->(langs:LanguageData)
            RETURN s, collect(langs) as languages
            LIMIT 1
                        """)
    Optional<Synset> findSynsetByAnyWordForm(String word);

    Optional<Synset> findSynsetBySynsetId(String synsetId);
}
