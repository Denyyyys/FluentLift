package com.denyyyys.fluentLift.model.neo4j.dto.polish;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "aspect", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = JsonPolishPerfectiveVerb.class, name = "perfective"),
        @JsonSubTypes.Type(value = JsonPolishImperfectiveVerb.class, name = "imperfective")
})

@Getter
@Setter
public abstract class JsonPolishVerbConjugation {

    private String lemma;
    private String aspect;

    @JsonProperty("aspect_pair")
    private String aspectPair;

    @JsonProperty("past_tense")
    private PolishPastTense pastTense;
    private PolishConditionalTense conditional;
    private PolishImperative imperative;

    @JsonProperty("passive_adjectival_participle")
    private String passiveAdjectivalParticiple;
    private String gerund;

    @Getter
    @Setter
    public static class GenderForms {
        @JsonProperty("1sg")
        private String firstSingular;
        @JsonProperty("2sg")
        private String secondSingular;
        @JsonProperty("3sg")
        private String thirdSingular;
    }

    @Getter
    @Setter
    public static class PersonForms {
        @JsonProperty("1pl")
        private String firstPlural;
        @JsonProperty("2pl")
        private String secondPlural;
        @JsonProperty("3pl")
        private String thirdPlural;
    }

    @Getter
    @Setter
    public static class PolishPastTense {

        private SingularPast singular;
        private PluralPast plural;

        @Getter
        @Setter
        public static class SingularPast {
            private GenderForms masculine;
            private GenderForms feminine;
            private GenderForms neuter;
        }

        @Getter
        @Setter
        public static class PluralPast {
            @JsonProperty("masculine_personal")
            private PersonForms masculinePersonal;

            @JsonProperty("non_masculine_personal")
            private PersonForms nonMasculinePersonal;
        }

    }

    @Getter
    @Setter
    public static class PolishImperative {
        private SingularImperative singular;
        private PluralImperative plural;

        @Getter
        @Setter
        public static class SingularImperative {
            @JsonProperty("2sg")
            private String secondSingular;
        }

        @Getter
        @Setter
        public static class PluralImperative {
            @JsonProperty("1pl")
            private String firstPlural;
            @JsonProperty("2pl")
            private String secondPlural;
        }
    }

    @Getter
    @Setter
    public static class PolishConditionalTense {
        private SingularConditional singular;
        private PluralConditional plural;

        @Getter
        @Setter
        public static class SingularConditional {
            private GenderForms masculine;
            private GenderForms feminine;
            private GenderForms neuter;
        }

        @Getter
        @Setter
        public static class PluralConditional {
            @JsonProperty("masculine_personal")
            private PersonForms masculinePersonal;

            @JsonProperty("non_masculine_personal")
            private PersonForms nonMasculinePersonal;
        }

    }

}
