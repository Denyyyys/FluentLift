package com.denyyyys.fluentLift.repo.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.MultipleChoiceOption;

public interface MultipleChoiceOptionRepository extends JpaRepository<MultipleChoiceOption, Long> {

    // @Query("""
    // SELECT mco FROM MultipleChoiceOption mco
    // WHERE mco.id IN :ids
    // """)
    // public List<MultipleChoiceOption> findAllById(@Param("ids") List<Long> ids);
    public List<MultipleChoiceOption> findAllByIdIn(List<Long> ids);
}
