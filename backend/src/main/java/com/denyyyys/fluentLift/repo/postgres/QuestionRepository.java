package com.denyyyys.fluentLift.repo.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.denyyyys.fluentLift.model.postgres.entity.questionsAndAnswers.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
            SELECT q FROM Question q
            LEFT JOIN FETCH q.answers a
            LEFT JOIN FETCH a.replies r
            WHERE q.id = :id
            """)
    Question findQuestionWithAllAnswers(@Param("id") Long id);

}
