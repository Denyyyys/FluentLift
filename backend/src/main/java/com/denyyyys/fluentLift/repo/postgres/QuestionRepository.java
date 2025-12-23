package com.denyyyys.fluentLift.repo.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
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

        @Query("""
                        SELECT q FROM Question q
                        LEFT JOIN q.tags t
                        WHERE (:tags IS NULL OR t IN :tags)
                            AND (:query IS NULL OR q.title LIKE CONCAT('%', :query, '%') OR q.content LIKE CONCAT('%', :query, '%'))
                            AND (:isSolved IS NULL OR q.solved = :isSolved)
                        GROUP BY (q)
                        HAVING COUNT(DISTINCT t) >= :tagCount
                        """)
        Page<Question> searchQuestions(
                        @Param("query") String query,
                        @Param("tags") List<String> tags,
                        @Param("tagCount") long tagCount,
                        @Param("isSolved") Boolean isSolved,
                        Pageable pageable);

}
