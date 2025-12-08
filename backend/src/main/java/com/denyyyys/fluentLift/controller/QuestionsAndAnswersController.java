package com.denyyyys.fluentLift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.AnswerCreateRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.QuestionCreateRequestDto;
import com.denyyyys.fluentLift.service.QuestionsAndAnswersService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/qa")
@RequiredArgsConstructor
public class QuestionsAndAnswersController {
    private final QuestionsAndAnswersService quesionsAndAnswersService;

    @PostMapping("/questions")
    public ResponseEntity<String> createQuestion(@AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody QuestionCreateRequestDto questionDto) {
        quesionsAndAnswersService.createQuestion(questionDto, user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created new question");
    }

    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<String> createAnswer(@AuthenticationPrincipal UserDetails user,
            @RequestBody AnswerCreateRequestDto answerDto, @PathVariable Long questionId) {
        quesionsAndAnswersService.createAnswer(answerDto, questionId, user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created new answer");
    }

}
