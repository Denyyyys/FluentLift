package com.denyyyys.fluentLift.service;

import java.io.File;
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

}
