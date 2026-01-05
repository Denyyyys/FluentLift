package com.denyyyys.fluentLift.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.denyyyys.fluentLift.model.neo4j.dto.JsonSynset;
import com.denyyyys.fluentLift.service.SynsetService;

import lombok.RequiredArgsConstructor;

@Profile("translateSynsets")
@Configuration
@RequiredArgsConstructor
public class SynsetExamplesTranslateRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(SynsetExamplesTranslateRunner.class);

    private final SynsetService synsetService;

    private String synsetsFilePath = "/home/denys/langApp/web_scraper/data/synsets/synsets.json";

    private String synsetsWithTranslatedExamplesFilePath = "/home/denys/langApp/web_scraper/data/synsets/synsetsWithTranslatedExamples.json";

    @Override
    public void run(String... args) throws Exception {
        log.info("Running DeepL synset translation...");

        List<JsonSynset> jsonSynsets = synsetService.loadSynsets(this.synsetsFilePath, 5, 10);

        jsonSynsets.stream().forEach(synset -> {
            try {
                synsetService.translateSynsetEnToPlAndUk(synset);
            } catch (Exception e) {
                throw new RuntimeException("DeepL translation failed", e);
            }
        });

        log.info("DeepL synset translation finished");

        log.info(jsonSynsets.getFirst().getPL().getExamples().toString());
        log.info(jsonSynsets.getFirst().getUK().getExamples().toString());

        synsetService.saveSynsetsToJsonFile(jsonSynsets, this.synsetsWithTranslatedExamplesFilePath);
    }

}
