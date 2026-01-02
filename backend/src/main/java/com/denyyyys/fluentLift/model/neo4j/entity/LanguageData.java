package com.denyyyys.fluentLift.model.neo4j.entity;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Node("LanguageData")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LanguageData {
    @Id
    private String Id;

    private String language;
    private String lemma;
    private List<String> synonyms;
    private String definition;
    private List<String> examples;
}
