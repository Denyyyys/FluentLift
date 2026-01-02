package com.denyyyys.fluentLift.model.neo4j.entity;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Node("Synset")
@Getter
@Setter
public class Synset {
    @Id
    @JsonProperty("synset_id")
    private String synsetId;

    private String pos;

    @Relationship(type = "HAS_LANGUAGE")
    private List<LanguageData> languages;
}
