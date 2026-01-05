package com.denyyyys.fluentLift.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.denyyyys.fluentLift.model.neo4j.dto.JsonSynset;
import com.denyyyys.fluentLift.model.neo4j.entity.Synset;
import com.denyyyys.fluentLift.repo.neo4j.SynsetRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SynsetService {
    private final SynsetRepository synsetRepository;
    private final ObjectMapper objectMapper;

    private final DeepLTranslateService deepLTranslateService;

    public Optional<Synset> findByLemma(String lemma) {
        return synsetRepository.findFirstByLanguagesLemma(lemma);
    }

    public List<JsonSynset> loadSynsets(String filePath, int n, int toSkip) throws Exception {
        List<JsonSynset> allSynsets = objectMapper.readValue(
                new File(filePath),
                new TypeReference<List<JsonSynset>>() {

                });

        int fromIndex = Math.min(toSkip, allSynsets.size());
        int toIndex = Math.min(fromIndex + n, allSynsets.size());

        return allSynsets.subList(fromIndex, toIndex);
    }

    public void saveSynsetsToJsonFile(List<JsonSynset> synsets, String outputPath) throws IOException {
        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(new File(outputPath), synsets);
    }

    public void translateSynsetEnToPlAndUk(JsonSynset synset) throws Exception {
        List<String> enExamples = Optional.ofNullable(synset.getEN())
                .map(JsonSynset.JsonLanguageField::getExamples)
                .orElse(List.of());

        if (enExamples.isEmpty()) {
            return;
        }

        String enDefinition = synset.getEN().getDefinition();

        if (!synset.getPL().getLemma().isEmpty()) {
            String plDefinition = synset.getPL().getDefinition();

            if (plDefinition.isBlank() || plDefinition == null) {
                synset.getPL().setDefinition(deepLTranslateService.translateEnToPl(enDefinition));
            }

            List<String> plExamples = enExamples.stream()
                    .map(example -> {
                        try {
                            return deepLTranslateService.translateEnToPl(example);
                        } catch (Exception e) {
                            throw new RuntimeException("DeepL translation from EN to PL failed", e);
                        }
                    })
                    .toList();

            synset.getPL().setExamples(plExamples);
        }

        if (!synset.getUK().getLemma().isEmpty()) {
            String ukDefinition = synset.getUK().getDefinition();

            if (ukDefinition.isBlank() || ukDefinition == null) {
                synset.getUK().setDefinition(deepLTranslateService.translateEnToUk(enDefinition));
            }

            List<String> ukExamples = enExamples.stream()
                    .map(example -> {
                        try {
                            return deepLTranslateService.translateEnToUk(example);
                        } catch (Exception e) {
                            throw new RuntimeException("DeepL translation from EN to UK failed", e);
                        }
                    })
                    .toList();

            synset.getUK().setExamples(ukExamples);
        }
    }

}
