package com.denyyyys.fluentLift.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import com.denyyyys.fluentLift.model.neo4j.dto.JsonSynset;
import com.denyyyys.fluentLift.model.neo4j.entity.Synset;
import com.denyyyys.fluentLift.model.neo4j.mapper.SynsetMapper;
import com.denyyyys.fluentLift.repo.neo4j.SynsetRepository;
import com.denyyyys.fluentLift.service.SynsetService;

@Profile("seedSynsetsNeo4j")
@Configuration
@Order(1)
public class SynsetSeedRunner {
    private static final Logger log = LoggerFactory.getLogger(SynsetSeedRunner.class);

    @Bean
    CommandLineRunner importSynsets(SynsetService synsetService, SynsetRepository repository) {
        return args -> {
            log.info("Running Neo4j synset seeding...");
            // String synsetsFilePath =
            // "/home/denys/langApp/web_scraper/data/synsets/synsetsWithTranslatedExamples.json";
            String synsetsFilePath = "./synsetsWithTranslatedExamples.json";

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

        };
    }
}
