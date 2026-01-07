package com.denyyyys.fluentLift.model.neo4j.entity.polish;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.denyyyys.fluentLift.model.neo4j.entity.LanguageData;

import lombok.Getter;
import lombok.Setter;

@Node("PolishImperfectiveVerb")
@Getter
@Setter
public class PolishImperfectiveVerbNode {
    @Id
    private String lemma;

    @Relationship(type = "CONJUGATION_OF", direction = Relationship.Direction.OUTGOING)
    private LanguageData languageData;

    private String aspect; // "imperfective"
    private String aspectPair;

    /*
     * =========================
     * PRESENT TENSE
     * =========================
     */

    private String pres_sg_1;
    private String pres_sg_2;
    private String pres_sg_3;

    private String pres_pl_1;
    private String pres_pl_2;
    private String pres_pl_3;

    /*
     * =========================
     * FUTURE TENSE
     * =========================
     */

    // singular – masculine
    private List<String> fut_sg_m_1;
    private List<String> fut_sg_m_2;
    private List<String> fut_sg_m_3;

    // singular – feminine
    private List<String> fut_sg_f_1;
    private List<String> fut_sg_f_2;
    private List<String> fut_sg_f_3;

    // singular – neuter
    private List<String> fut_sg_n_1;
    private List<String> fut_sg_n_2;
    private List<String> fut_sg_n_3;

    // plural – masculine personal
    private List<String> fut_pl_mp_1;
    private List<String> fut_pl_mp_2;
    private List<String> fut_pl_mp_3;

    // plural – non masculine personal
    private List<String> fut_pl_nmp_1;
    private List<String> fut_pl_nmp_2;
    private List<String> fut_pl_nmp_3;

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

    private String activeAdjectivalParticiple;
    private String passiveAdjectivalParticiple;
    private String contemporaryAdverbialParticiple;
    private String gerund;
}