package com.denyyyys.fluentLift.repo.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.MultipleChoiceBlock;

public interface MultipleChoiceBlockRepository extends JpaRepository<MultipleChoiceBlock, Long> {
    @Query("SELECT mcb.id FROM MultipleChoiceBlock mcb WHERE mcb.lesson.id IN :lessonIds")
    List<Long> findIdsForLessonIds(@Param("lessonIds") List<Long> lessonIds);

}
