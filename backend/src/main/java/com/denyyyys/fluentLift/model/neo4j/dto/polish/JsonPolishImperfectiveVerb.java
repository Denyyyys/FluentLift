package com.denyyyys.fluentLift.model.neo4j.dto.polish;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonPolishImperfectiveVerb extends JsonPolishVerbConjugation {

    @JsonProperty("future_tense")
    private PolishImperfectiveFutureTense futureTense;

    @JsonProperty("present_tense")
    private PolishImperfectivePresentTense presentTense;

    @JsonProperty("active_adjectival_participle")
    private String activeAdjectivalParticiple;

    @JsonProperty("contemporary_adverbial_participle")
    private String contemporaryAdverbialParticiple;

    @Getter
    @Setter
    public class PolishImperfectiveFutureTense {
        private SingularFuture singular;
        private PluralFuture plural;

        @Getter
        @Setter
        public class SingularFuture {
            private GenderFormsAsList masculine;
            private GenderFormsAsList feminine;
            private GenderFormsAsList neuter;
        }

        @Getter
        @Setter
        public class PluralFuture {
            @JsonProperty("masculine_personal")
            private PersonFormsAsList masculinePersonal;

            @JsonProperty("non_masculine_personal")
            private PersonFormsAsList nonMasculinePersonal;
        }

        @Getter
        @Setter
        public static class GenderFormsAsList {
            @JsonProperty("1sg")
            private List<String> firstSingular;
            @JsonProperty("2sg")
            private List<String> secondSingular;
            @JsonProperty("3sg")
            private List<String> thirdSingular;
        }

        @Getter
        @Setter
        public static class PersonFormsAsList {
            @JsonProperty("1pl")
            private List<String> firstPlural;
            @JsonProperty("2pl")
            private List<String> secondPlural;
            @JsonProperty("3pl")
            private List<String> thirdPlural;
        }
    }

    @Getter
    @Setter
    public class PolishImperfectivePresentTense {

        private GenderForms singular;
        private PersonForms plural;

    }

}
