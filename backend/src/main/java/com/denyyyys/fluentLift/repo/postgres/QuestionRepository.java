package com.denyyyys.fluentLift.repo.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.denyyyys.fluentLift.model.postgres.entity.questionsAndAnswers.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
            SELECT q FROM Question q
            LEFT JOIN FETCH q.answers a
            WHERE q.id = :id
            """)
    Optional<Question> findQuestionWithAllAnswers(@Param("id") Long id);

    List<Question> findAllBySolved(boolean solved, Pageable pageable);
}
