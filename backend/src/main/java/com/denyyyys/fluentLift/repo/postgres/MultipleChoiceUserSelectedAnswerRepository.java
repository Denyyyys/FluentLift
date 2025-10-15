package com.denyyyys.fluentLift.repo.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.MultipleChoiceUserSelectedAnswer;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.MultipleChoiceUserSelectedAnswerId;

public interface MultipleChoiceUserSelectedAnswerRepository
        extends JpaRepository<MultipleChoiceUserSelectedAnswer, MultipleChoiceUserSelectedAnswerId> {

    @Query("""
            SELECT mcusa.multipleChoiceOption.id FROM MultipleChoiceUserSelectedAnswer mcusa
            WHERE mcusa.user.id = :userId AND
            mcusa.multipleChoiceOption.multipleChoiceBlock.lesson.unit.course.id = :courseId
            """)
    List<Long> findMultipleChoiceOptionIdsForUserIdAndCourseId(
            @Param("userId") Long userId,
            @Param("courseId") Long courseId);

    @Query("""
            SELECT DISTINCT mcusa.multipleChoiceOption.multipleChoiceBlock.id FROM MultipleChoiceUserSelectedAnswer mcusa
            WHERE mcusa.user.id = :userId AND
            mcusa.multipleChoiceOption.multipleChoiceBlock.lesson.unit.course.id = :courseId
            """)
    List<Long> findMultipleChoiceBlockIdsForUserIdAndCourseId(
            @Param("userId") Long userId,
            @Param("courseId") Long courseId);
}
