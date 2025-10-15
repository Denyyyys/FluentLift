package com.denyyyys.fluentLift.repo.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.ClozeBlockUserAnswer;

public interface ClozeBlockUserAnswerRepository extends JpaRepository<ClozeBlockUserAnswer, Long> {
    @Query("""
                SELECT cbua FROM ClozeBlockUserAnswer cbua
                WHERE cbua.user.id = :userId AND
                cbua.clozeBlockAnswer.cloze.lesson.unit.course.id = :courseId
            """)
    List<ClozeBlockUserAnswer> findForUserIdAndCourseId(@Param("userId") Long userId,
            @Param("courseId") Long courseId);

}
