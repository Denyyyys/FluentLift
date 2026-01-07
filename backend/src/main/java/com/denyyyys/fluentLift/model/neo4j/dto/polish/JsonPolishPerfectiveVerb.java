package com.denyyyys.fluentLift.model.neo4j.dto.polish;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonPolishPerfectiveVerb extends JsonPolishVerbConjugation {
    @JsonProperty("future_tense")
    private PolishPerfectiveFutureTense futureTense;

    @JsonProperty("anterior adverbial participle")
    private String anteriorAdverbialParticiple;

    @Getter
    @Setter
    public class PolishPerfectiveFutureTense {
        private GenderForms singular;
        private PersonForms plural;

    }
}
