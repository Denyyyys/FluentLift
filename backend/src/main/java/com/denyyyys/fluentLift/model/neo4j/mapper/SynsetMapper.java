package com.denyyyys.fluentLift.model.neo4j.mapper;

import java.util.ArrayList;
import java.util.List;

import com.denyyyys.fluentLift.model.neo4j.dto.JsonSynset;
import com.denyyyys.fluentLift.model.neo4j.entity.LanguageData;
import com.denyyyys.fluentLift.model.neo4j.entity.Synset;

public class SynsetMapper {

    private static LanguageData mapLanguage(JsonSynset.JsonLanguageField field, String synsetId, String langCode) {
        LanguageData ld = new LanguageData();
        ld.setId(synsetId + "_" + langCode);
        ld.setLanguage(langCode);

        if (field == null) {
            ld.setLemma("");
            ld.setSynonyms(List.of());
            ld.setDefinition("");
            ld.setExamples(List.of());
            return ld;
        }

        ld.setLemma(field.getLemma() == null ? "" : field.getLemma());
        ld.setSynonyms(field.getSynonyms() != null ? field.getSynonyms() : List.of());
        ld.setDefinition(field.getDefinition() == null ? "" : field.getDefinition());
        ld.setExamples(field.getExamples() != null ? field.getExamples() : List.of());

        return ld;
    }

    public static Synset toEntity(JsonSynset jsonSynset) {
        Synset synset = new Synset();
        synset.setSynsetId(jsonSynset.getSynset_id());
        synset.setPos(jsonSynset.getPos());

        List<LanguageData> languages = new ArrayList<>();

        languages.add(mapLanguage(jsonSynset.getEN(), jsonSynset.getSynset_id(), "EN"));
        languages.add(mapLanguage(jsonSynset.getPL(), jsonSynset.getSynset_id(), "PL"));
        languages.add(mapLanguage(jsonSynset.getUK(), jsonSynset.getSynset_id(), "UK"));

        synset.setLanguages(languages);
        return synset;
    }
}
