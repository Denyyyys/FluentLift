package com.denyyyys.fluentLift.model.neo4j.mapper;

import java.util.List;
import java.util.function.Consumer;

import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishImperfectiveVerb;
import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishImperfectiveVerb.PolishImperfectiveFutureTense.GenderFormsAsList;
import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishImperfectiveVerb.PolishImperfectiveFutureTense.PersonFormsAsList;
import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishPerfectiveVerb;
import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishVerbConjugation.GenderForms;
import com.denyyyys.fluentLift.model.neo4j.dto.polish.JsonPolishVerbConjugation.PersonForms;
import com.denyyyys.fluentLift.model.neo4j.entity.polish.PolishImperfectiveVerbNode;
import com.denyyyys.fluentLift.model.neo4j.entity.polish.PolishPerfectiveVerbNode;

public final class PolishVerbMapper {

    private PolishVerbMapper() {
    }

    public static PolishImperfectiveVerbNode toEntity(JsonPolishImperfectiveVerb json) {
        PolishImperfectiveVerbNode node = new PolishImperfectiveVerbNode();

        /* ========= BASIC ========= */

        node.setLemma(json.getLemma());
        node.setAspect(json.getAspect());
        node.setAspectPair(json.getAspectPair());
        node.setGerund(json.getGerund());
        node.setActiveAdjectivalParticiple(json.getActiveAdjectivalParticiple());
        node.setPassiveAdjectivalParticiple(json.getPassiveAdjectivalParticiple());
        node.setContemporaryAdverbialParticiple(json.getContemporaryAdverbialParticiple());
        /* ========= PRESENT ========= */

        if (json.getPresentTense() != null) {
            var s = json.getPresentTense().getSingular();
            var p = json.getPresentTense().getPlural();

            if (s != null) {
                node.setPres_sg_1(s.getFirstSingular());
                node.setPres_sg_2(s.getSecondSingular());
                node.setPres_sg_3(s.getThirdSingular());
            }

            if (p != null) {
                node.setPres_pl_1(p.getFirstPlural());
                node.setPres_pl_2(p.getSecondPlural());
                node.setPres_pl_3(p.getThirdPlural());
            }
        }

        /* ========= FUTURE ========= */

        if (json.getFutureTense() != null) {
            var fs = json.getFutureTense().getSingular();
            var fp = json.getFutureTense().getPlural();

            if (fs != null) {
                mapGenderFormsAsList(fs.getMasculine(),
                        node::setFut_sg_m_1,
                        node::setFut_sg_m_2,
                        node::setFut_sg_m_3);

                mapGenderFormsAsList(fs.getFeminine(),
                        node::setFut_sg_f_1,
                        node::setFut_sg_f_2,
                        node::setFut_sg_f_3);

                mapGenderFormsAsList(fs.getNeuter(),
                        node::setFut_sg_n_1,
                        node::setFut_sg_n_2,
                        node::setFut_sg_n_3);
            }

            if (fp != null) {
                mapPersonFormsAsList(fp.getMasculinePersonal(),
                        node::setFut_pl_mp_1,
                        node::setFut_pl_mp_2,
                        node::setFut_pl_mp_3);

                mapPersonFormsAsList(fp.getNonMasculinePersonal(),
                        node::setFut_pl_nmp_1,
                        node::setFut_pl_nmp_2,
                        node::setFut_pl_nmp_3);
            }
        }

        /* ========= PAST ========= */

        if (json.getPastTense() != null) {
            var s = json.getPastTense().getSingular();
            var p = json.getPastTense().getPlural();

            if (s != null) {
                mapGenderForms(s.getMasculine(),
                        node::setPast_sg_m_1,
                        node::setPast_sg_m_2,
                        node::setPast_sg_m_3);

                mapGenderForms(s.getFeminine(),
                        node::setPast_sg_f_1,
                        node::setPast_sg_f_2,
                        node::setPast_sg_f_3);

                mapGenderForms(s.getNeuter(),
                        node::setPast_sg_n_1,
                        node::setPast_sg_n_2,
                        node::setPast_sg_n_3);
            }

            if (p != null) {
                mapPersonForms(p.getMasculinePersonal(),
                        node::setPast_pl_mp_1,
                        node::setPast_pl_mp_2,
                        node::setPast_pl_mp_3);

                mapPersonForms(p.getNonMasculinePersonal(),
                        node::setPast_pl_nmp_1,
                        node::setPast_pl_nmp_2,
                        node::setPast_pl_nmp_3);
            }
        }

        /* ========= CONDITIONAL ========= */

        if (json.getConditional() != null) {
            var s = json.getConditional().getSingular();
            var p = json.getConditional().getPlural();

            if (s != null) {
                mapGenderForms(s.getMasculine(),
                        node::setCond_sg_m_1,
                        node::setCond_sg_m_2,
                        node::setCond_sg_m_3);

                mapGenderForms(s.getFeminine(),
                        node::setCond_sg_f_1,
                        node::setCond_sg_f_2,
                        node::setCond_sg_f_3);

                mapGenderForms(s.getNeuter(),
                        node::setCond_sg_n_1,
                        node::setCond_sg_n_2,
                        node::setCond_sg_n_3);
            }

            if (p != null) {
                mapPersonForms(p.getMasculinePersonal(),
                        node::setCond_pl_mp_1,
                        node::setCond_pl_mp_2,
                        node::setCond_pl_mp_3);

                mapPersonForms(p.getNonMasculinePersonal(),
                        node::setCond_pl_nmp_1,
                        node::setCond_pl_nmp_2,
                        node::setCond_pl_nmp_3);
            }
        }

        /* ========= IMPERATIVE ========= */

        if (json.getImperative() != null) {
            if (json.getImperative().getSingular() != null) {
                node.setImp_sg_2(json.getImperative().getSingular().getSecondSingular());
            }
            if (json.getImperative().getPlural() != null) {
                node.setImp_pl_1(json.getImperative().getPlural().getFirstPlural());
                node.setImp_pl_2(json.getImperative().getPlural().getSecondPlural());
            }
        }

        return node;
    }

    public static PolishPerfectiveVerbNode toEntity(JsonPolishPerfectiveVerb json) {
        PolishPerfectiveVerbNode node = new PolishPerfectiveVerbNode();

        node.setLemma(json.getLemma());
        node.setAspect(json.getAspect());
        node.setAspectPair(json.getAspectPair());

        node.setPassiveAdjectivalParticiple(json.getPassiveAdjectivalParticiple());
        node.setAnteriorAdverbialParticiple(json.getAnteriorAdverbialParticiple());
        node.setGerund(json.getGerund());

        // ===== PAST TENSE =====

        if (json.getPastTense() != null) {
            var s = json.getPastTense().getSingular();
            var p = json.getPastTense().getPlural();

            if (s != null) {
                mapGenderForms(s.getMasculine(),
                        node::setPast_sg_m_1,
                        node::setPast_sg_m_2,
                        node::setPast_sg_m_3);

                mapGenderForms(s.getFeminine(),
                        node::setPast_sg_f_1,
                        node::setPast_sg_f_2,
                        node::setPast_sg_f_3);

                mapGenderForms(s.getNeuter(),
                        node::setPast_sg_n_1,
                        node::setPast_sg_n_2,
                        node::setPast_sg_n_3);
            }

            if (p != null) {
                mapPersonForms(p.getMasculinePersonal(),
                        node::setPast_pl_mp_1,
                        node::setPast_pl_mp_2,
                        node::setPast_pl_mp_3);

                mapPersonForms(p.getNonMasculinePersonal(),
                        node::setPast_pl_nmp_1,
                        node::setPast_pl_nmp_2,
                        node::setPast_pl_nmp_3);
            }
        }

        // ===== CONDITIONAL =====

        if (json.getConditional() != null) {
            var s = json.getConditional().getSingular();
            var p = json.getConditional().getPlural();

            if (s != null) {
                mapGenderForms(s.getMasculine(),
                        node::setCond_sg_m_1,
                        node::setCond_sg_m_2,
                        node::setCond_sg_m_3);

                mapGenderForms(s.getFeminine(),
                        node::setCond_sg_f_1,
                        node::setCond_sg_f_2,
                        node::setCond_sg_f_3);

                mapGenderForms(s.getNeuter(),
                        node::setCond_sg_n_1,
                        node::setCond_sg_n_2,
                        node::setCond_sg_n_3);
            }

            if (p != null) {
                mapPersonForms(p.getMasculinePersonal(),
                        node::setCond_pl_mp_1,
                        node::setCond_pl_mp_2,
                        node::setCond_pl_mp_3);

                mapPersonForms(p.getNonMasculinePersonal(),
                        node::setCond_pl_nmp_1,
                        node::setCond_pl_nmp_2,
                        node::setCond_pl_nmp_3);
            }
        }

        // ===== FUTURE =====
        if (json.getFutureTense() != null) {
            var s = json.getFutureTense().getSingular();
            var p = json.getFutureTense().getPlural();

            if (s != null) {
                node.setFut_sg_1(s.getFirstSingular());
                node.setFut_sg_2(s.getSecondSingular());
                node.setFut_sg_3(s.getThirdSingular());
            }

            if (p != null) {
                node.setFut_pl_1(p.getFirstPlural());
                node.setFut_pl_2(p.getSecondPlural());
                node.setFut_pl_3(p.getThirdPlural());
            }
        }

        // ===== IMPERATIVE =====
        if (json.getImperative() != null) {
            if (json.getImperative().getSingular() != null) {
                node.setImp_sg_2(json.getImperative().getSingular().getSecondSingular());
            }
            if (json.getImperative().getPlural() != null) {
                node.setImp_pl_1(json.getImperative().getPlural().getFirstPlural());
                node.setImp_pl_2(json.getImperative().getPlural().getSecondPlural());
            }
        }

        return node;
    }
    /* ================= HELPERS ================= */

    private static void mapGenderForms(
            GenderForms src,
            Consumer<String> firstSingularSetter,
            Consumer<String> secondSingularSetter,
            Consumer<String> thirdSingularSetter) {
        if (src == null) {
            return;
        }
        firstSingularSetter.accept(src.getFirstSingular());
        secondSingularSetter.accept(src.getSecondSingular());
        thirdSingularSetter.accept(src.getThirdSingular());
    }

    private static void mapGenderFormsAsList(
            GenderFormsAsList src,
            Consumer<List<String>> firstSingularSetter,
            Consumer<List<String>> secondSingularSetter,
            Consumer<List<String>> thirdSingularSetter) {
        if (src == null) {
            return;
        }
        firstSingularSetter.accept(src.getFirstSingular());
        secondSingularSetter.accept(src.getSecondSingular());
        thirdSingularSetter.accept(src.getThirdSingular());
    }

    private static void mapPersonForms(
            PersonForms src,
            Consumer<String> firstPluralSetter,
            Consumer<String> secondPluralSetter,
            Consumer<String> thirdPluralSetter) {
        if (src == null) {
            return;
        }
        firstPluralSetter.accept(src.getFirstPlural());
        secondPluralSetter.accept(src.getSecondPlural());
        thirdPluralSetter.accept(src.getThirdPlural());
    }

    private static void mapPersonFormsAsList(
            PersonFormsAsList src,
            Consumer<List<String>> firstPluralSetter,
            Consumer<List<String>> secondPluralSetter,
            Consumer<List<String>> thirdPluralSetter) {
        if (src == null) {
            return;
        }
        firstPluralSetter.accept(src.getFirstPlural());
        secondPluralSetter.accept(src.getSecondPlural());
        thirdPluralSetter.accept(src.getThirdPlural());
    }

}
