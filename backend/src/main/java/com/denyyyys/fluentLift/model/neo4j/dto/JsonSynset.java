package com.denyyyys.fluentLift.model.neo4j.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonSynset {
    private String synset_id;
    private String pos;

    @JsonProperty("EN")
    private JsonLanguageField EN;

    @JsonProperty("PL")
    private JsonLanguageField PL;

    @JsonProperty("UK")
    private JsonLanguageField UK;

    @Getter
    @Setter
    public static class JsonLanguageField {
        private String lemma;
        private List<String> synonyms;
        private String definition;
        private List<String> examples;
    }
}
