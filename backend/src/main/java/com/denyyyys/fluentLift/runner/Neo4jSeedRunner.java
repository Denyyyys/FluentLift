package com.denyyyys.fluentLift.runner;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.denyyyys.fluentLift.model.neo4j.dto.JsonSynset;
import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishImperfectiveVerb;
import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishPerfectiveVerb;
import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishVerbConjugation;
import com.denyyyys.fluentLift.model.neo4j.entity.Synset;
import com.denyyyys.fluentLift.model.neo4j.entity.polish.PolishImperfectiveVerbNode;
import com.denyyyys.fluentLift.model.neo4j.entity.polish.PolishPerfectiveVerbNode;
import com.denyyyys.fluentLift.model.neo4j.mapper.PolishVerbMapper;
import com.denyyyys.fluentLift.model.neo4j.mapper.SynsetMapper;
import com.denyyyys.fluentLift.repo.neo4j.PolishImperfectiveVerbRepository;
import com.denyyyys.fluentLift.repo.neo4j.PolishPerfectiveVerbRepository;
import com.denyyyys.fluentLift.repo.neo4j.SynsetRepository;
import com.denyyyys.fluentLift.service.SynsetService;

import lombok.RequiredArgsConstructor;

@Profile("seedNeo4j")
@Configuration
@RequiredArgsConstructor
public class Neo4jSeedRunner {
    private static final Logger log = LoggerFactory.getLogger(SynsetSeedRunner.class);

    private final SynsetService synsetService;
    private final SynsetRepository repository;
    private final PolishPerfectiveVerbRepository perfectiveVerbRepository;
    private final PolishImperfectiveVerbRepository imperfectiveVerbRepository;

    private String conjugationFilePath = "./polish_verbs_conjugation.json";
    private String synsetsFilePath = "./synsetsWithTranslatedExamples.json";

    @Bean
    CommandLineRunner importSynsets() {
        return args -> {
            log.info("Running Neo4j synset seeding...");

            List<JsonSynset> jsonSynsets = synsetService.loadSynsets(
                    synsetsFilePath,
                    500,
                    0);

            try {
                Synset synset = synsetService.findSynsetByWord(jsonSynsets.getFirst().getEN().getLemma());
                if (synset != null) {
                    log.info("Synset found in repo. Skip seeding");
                    return;
                }
            } catch (Exception e) {
                log.error("Error during Neo4j synset seeding: ");
                log.error(e.getMessage());
                log.error(e.getStackTrace().toString());
            }

            List<Synset> synsets = jsonSynsets.stream().map(SynsetMapper::toEntity).toList();
            repository.saveAll(synsets);

            log.info("Neo4j synset seeding finished");

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
        };
    }

}
