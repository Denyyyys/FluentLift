package com.denyyyys.fluentLift.repo.postgres;

import org.springframework.data.jpa.repository.JpaRepository;

import com.denyyyys.fluentLift.model.postgres.entity.questionsAndAnswers.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
