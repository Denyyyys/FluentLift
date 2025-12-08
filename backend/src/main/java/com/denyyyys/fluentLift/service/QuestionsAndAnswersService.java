package com.denyyyys.fluentLift.service;

import org.springframework.stereotype.Service;

import com.denyyyys.fluentLift.exceptions.ResourceNotFound;
import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.AnswerCreateRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.QuestionCreateRequestDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.questionsAndAnswers.Answer;
import com.denyyyys.fluentLift.model.postgres.entity.questionsAndAnswers.Question;
import com.denyyyys.fluentLift.model.postgres.mapper.QuestionsAndAnswersMapper;
import com.denyyyys.fluentLift.repo.postgres.AnswerRepository;
import com.denyyyys.fluentLift.repo.postgres.AppUserRepository;
import com.denyyyys.fluentLift.repo.postgres.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionsAndAnswersService {
    private final AppUserRepository appUserRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public void createQuestion(QuestionCreateRequestDto questionDto, String authorEmail) {
        AppUser author = appUserRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new ResourceNotFound("Author not found"));

        Question question = QuestionsAndAnswersMapper.toEntity(questionDto, author);
        questionRepository.save(question);
    }

    public void createAnswer(AnswerCreateRequestDto answerDto, Long questionId, String authorEmail) {
        AppUser author = appUserRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new ResourceNotFound("Author not found"));

        Answer answer = QuestionsAndAnswersMapper.toEntity(answerDto, questionId, author);
        answerRepository.save(answer);
    }
}
