package com.denyyyys.fluentLift.runner;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishImperfectiveVerb;
import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishPerfectiveVerb;
import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishVerbConjugation;
import com.denyyyys.fluentLift.model.neo4j.entity.Synset;
import com.denyyyys.fluentLift.model.neo4j.entity.polish.PolishImperfectiveVerbNode;
import com.denyyyys.fluentLift.model.neo4j.entity.polish.PolishPerfectiveVerbNode;
import com.denyyyys.fluentLift.model.neo4j.mapper.PolishVerbMapper;
import com.denyyyys.fluentLift.repo.neo4j.PolishImperfectiveVerbRepository;
import com.denyyyys.fluentLift.repo.neo4j.PolishPerfectiveVerbRepository;
import com.denyyyys.fluentLift.service.SynsetService;

import lombok.RequiredArgsConstructor;

@Profile("seedPolishVerbConjugationNeo4j")
@Configuration
@RequiredArgsConstructor
@Order(2)
public class PolishVerbConjugationSeedRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(PolishVerbConjugationSeedRunner.class);

    private final SynsetService synsetService;

    private final PolishPerfectiveVerbRepository perfectiveVerbRepository;
    private final PolishImperfectiveVerbRepository imperfectiveVerbRepository;

    // private String conjugationFilePath =
    // "/home/denys/langApp/web_scraper/data/polish/polish_verbs_conjugation.json";
    private String conjugationFilePath = "./polish_verbs_conjugation.json";

    @Override
    public void run(String... args) throws Exception {
        log.info("Neo4j polish verbs conjugation seeding started...");

        List<JsonPolishVerbConjugation> verbs = synsetService.getListOfPolishVerbsFromFile(conjugationFilePath);

        List<PolishPerfectiveVerbNode> perfectiveVerbNodes = new ArrayList<>();
        List<PolishImperfectiveVerbNode> imperfectiveVerbNodes = new ArrayList<>();

        for (JsonPolishVerbConjugation verb : verbs) {
            if (verb instanceof JsonPolishPerfectiveVerb pv) {
                perfectiveVerbNodes.add(PolishVerbMapper.toEntity(pv));
            } else if (verb instanceof JsonPolishImperfectiveVerb impv) {
                imperfectiveVerbNodes.add(PolishVerbMapper.toEntity(impv));
            } else {
                throw new IllegalStateException("Unknown verb type: " + verb.getClass().getSimpleName());
            }

        }

        try {
            Synset synsetFromPerfective = synsetService.findSynsetByWord("opuszczając");
            if (synsetFromPerfective != null) {
                log.info("Found Polish forms for words from file in Neo4j db. Skip saving these data to the db");
                return;
            }
        } catch (Exception e) {
            log.error("Error during Neo4j polish verbs conjugation seeding: ");
            log.error(e.getMessage());
            log.error(e.getStackTrace().toString());
        }

        perfectiveVerbRepository.saveAll(perfectiveVerbNodes);
        imperfectiveVerbRepository.saveAll(imperfectiveVerbNodes);

        for (PolishImperfectiveVerbNode impv : imperfectiveVerbNodes) {
            imperfectiveVerbRepository.linkImperfectiveVerbToLanguageData(impv.getLemma());
        }

        for (PolishPerfectiveVerbNode impv : perfectiveVerbNodes) {
            perfectiveVerbRepository.linkPerfectiveVerbToLanguageData(impv.getLemma());
        }

        log.info("Neo4j polish verbs conjugation seeding finished");
    }

}
