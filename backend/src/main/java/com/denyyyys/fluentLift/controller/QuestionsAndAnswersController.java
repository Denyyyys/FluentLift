package com.denyyyys.fluentLift.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.denyyyys.fluentLift.config.Constants;
import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.AnswerCreateRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.QuestionCreateRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.VoteRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers.QuestionPageResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers.QuestionWithAnswersDto;
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

    @GetMapping("/questions")
    public ResponseEntity<QuestionPageResponseDto> getAllQuestions(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) Boolean isSolved,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = Constants.DEFAULT_SORT_QUESTIONS_BY) String sortBy,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_SIZE) int size) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(quesionsAndAnswersService.getAllQuestions(isSolved, query, tags, sortBy, page, size,
                        user.getUsername()));
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<QuestionWithAnswersDto> getQuestion(@PathVariable Long questionId) {
        return ResponseEntity.status(HttpStatus.OK).body(quesionsAndAnswersService.getQuestion(questionId));
    }

    @PostMapping("/questions/{questionId}/vote")
    public ResponseEntity<String> voteQuestion(@AuthenticationPrincipal UserDetails user, @PathVariable Long questionId,
            @RequestBody VoteRequestDto voteRequestDto) {
        quesionsAndAnswersService.voteQuestion(questionId, voteRequestDto.getVote());
        return ResponseEntity.ok().body("Successfully voted!");
    }

    @PostMapping("/answers/{answerId}/vote")
    public ResponseEntity<String> voteAnswer(@AuthenticationPrincipal UserDetails user, @PathVariable Long answerId,
            @RequestBody VoteRequestDto voteRequestDto) {
        quesionsAndAnswersService.voteAnswer(answerId, voteRequestDto.getVote());
        return ResponseEntity.ok().body("Successfully voted!");
    }

    @PostMapping("/questions/{questionId}/answers/{answerId}/accept")
    public ResponseEntity<String> acceptAnswer(@AuthenticationPrincipal UserDetails user, @PathVariable Long questionId,
            @PathVariable Long answerId) {
        quesionsAndAnswersService.acceptAnswer(questionId, answerId, user.getUsername());
        return ResponseEntity.ok("Answer accepted and question marked as solved!");
    }

    @PostMapping("/questions/{questionId}/addView")
    public ResponseEntity<Void> addView(@AuthenticationPrincipal UserDetails user, @PathVariable Long questionId) {
        quesionsAndAnswersService.addView(questionId, user.getUsername());

        return ResponseEntity.noContent().build();
    }

}
