package com.denyyyys.fluentLift.model.neo4j.entity.polish;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.denyyyys.fluentLift.model.neo4j.entity.LanguageData;

import lombok.Getter;
import lombok.Setter;

@Node("PolishPerfectiveVerb")
@Getter
@Setter
public class PolishPerfectiveVerbNode {
    @Id
    private String lemma;

    @Relationship(type = "CONJUGATION_OF", direction = Relationship.Direction.OUTGOING)
    private LanguageData languageData;

    private String aspect; // "perfective"
    private String aspectPair;

    /*
     * =========================
     * FUTURE TENSE
     * =========================
     */

    private String fut_sg_1;
    private String fut_sg_2;
    private String fut_sg_3;

    private String fut_pl_1;
    private String fut_pl_2;
    private String fut_pl_3;

    /*
     * =================
     * PAST TENSE
     * =================
     */

    // singular – masculine
    private String past_sg_m_1;
    private String past_sg_m_2;
    private String past_sg_m_3;

    // singular – feminine
    private String past_sg_f_1;
    private String past_sg_f_2;
    private String past_sg_f_3;

    // singular – neuter
    private String past_sg_n_1;
    private String past_sg_n_2;
    private String past_sg_n_3;

    // plural – masculine personal
    private String past_pl_mp_1;
    private String past_pl_mp_2;
    private String past_pl_mp_3;

    // plural – non masculine personal
    private String past_pl_nmp_1;
    private String past_pl_nmp_2;
    private String past_pl_nmp_3;

    /*
     * =================
     * CONDITIONAL
     * =================
     */

    // singular – masculine
    private String cond_sg_m_1;
    private String cond_sg_m_2;
    private String cond_sg_m_3;

    // singular – feminine
    private String cond_sg_f_1;
    private String cond_sg_f_2;
    private String cond_sg_f_3;

    // singular – neuter
    private String cond_sg_n_1;
    private String cond_sg_n_2;
    private String cond_sg_n_3;

    // plural – masculine personal
    private String cond_pl_mp_1;
    private String cond_pl_mp_2;
    private String cond_pl_mp_3;

    // plural – non masculine personal
    private String cond_pl_nmp_1;
    private String cond_pl_nmp_2;
    private String cond_pl_nmp_3;

    /*
     * =================
     * IMPERATIVE
     * =================
     */

    private String imp_sg_2;
    private String imp_pl_1;
    private String imp_pl_2;

    /*
     * =================
     * PARTICIPLES
     * =================
     */

    private String anteriorAdverbialParticiple;
    private String passiveAdjectivalParticiple;
    private String gerund;
}