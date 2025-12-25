package com.denyyyys.fluentLift.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denyyyys.fluentLift.config.Constants;
import com.denyyyys.fluentLift.exceptions.ResourceNotFound;
import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.AnswerCreateRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.QuestionCreateRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers.QuestionPageResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers.QuestionResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers.QuestionWithAnswersDto;
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

    public QuestionPageResponseDto getAllQuestions(Boolean isSolved, String query, List<String> tags, String sortBy,
            int page, int size,
            String userEmail) {
        appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        if (query == null) {
            query = "";
        }

        query.trim();
        tags = (tags == null || tags.isEmpty()) ? null : tags;

        String sortField = switch (sortBy) {
            case "upvotes" -> Constants.SORT_QUESTIONS_BY_UPVOTES;
            case "downvotes" -> Constants.SORT_QUESTIONS_BY_DOWNVOTES;
            default -> Constants.DEFAULT_SORT_QUESTIONS_BY;
        };

        size = Math.min(size, Constants.MAX_PAGE_SIZE);
        page = Math.max(page, 1);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortField).descending());
        Page<Question> questionPage = questionRepository.searchQuestions(
                query,
                (tags == null || tags.isEmpty()) ? null : tags,
                (tags == null || tags.isEmpty()) ? 0 : tags.size(),
                isSolved,
                pageable);

        List<QuestionResponseDto> questions = questionPage.getContent()
                .stream()
                .map(QuestionsAndAnswersMapper::toDto)
                .toList();

        QuestionPageResponseDto response = new QuestionPageResponseDto();
        response.setQuestions(questions);
        response.setPage(questionPage.getNumber() + 1);
        response.setSize(questionPage.getSize());
        response.setTotalElements(questionPage.getTotalElements());
        response.setTotalPages(questionPage.getTotalPages());

        return response;
    }

    public QuestionWithAnswersDto getQuestion(Long questionId) {
        Question question = questionRepository.findQuestionWithAllAnswers(questionId)
                .orElseThrow(() -> new ResourceNotFound("Question not found"));

        return QuestionsAndAnswersMapper.toQuestionWithAnswersDto(question);
    }

    public void voteQuestion(Long questionId, String vote) {
        vote = vote.toLowerCase();
        if (!vote.equals("up") && !vote.equals("down")) {
            throw new IllegalArgumentException("Vote must be 'up' or 'down'");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFound("Question not found"));

        if (vote.equals("up")) {
            question.setUpvotes(question.getUpvotes() + 1);
        } else {
            question.setDownvotes(question.getDownvotes() + 1);
        }

        questionRepository.save(question);
    }

    public void voteAnswer(Long answerId, String vote) {
        vote = vote.toLowerCase();
        if (!vote.equals("up") && !vote.equals("down")) {
            throw new IllegalArgumentException("Vote must be 'up' or 'down'");
        }

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFound("Answer not found"));

        if (vote.equals("up")) {
            answer.setUpvotes(answer.getUpvotes() + 1);
        } else {
            answer.setDownvotes(answer.getDownvotes() + 1);
        }
        answerRepository.save(answer);
    }

    @Transactional
    public void acceptAnswer(Long questionId, Long answerId, String userEmail) {
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        Question question = questionRepository.findQuestionWithAllAnswers(questionId)
                .orElseThrow(() -> new ResourceNotFound("Question not found"));

        if (question.getAuthor().getId() != user.getId()) {
            throw new AccessDeniedException("Only the question author can accept an answer");
        }

        Answer answerToAccept = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFound("Answer not found"));

        if (!answerToAccept.getQuestion().getId().equals(questionId)) {
            throw new IllegalArgumentException("Answer does not belong to this question");
        }

        boolean alreadyAccepted = question.getAnswers().stream().anyMatch(Answer::isAccepted);
        if (alreadyAccepted) {
            throw new IllegalStateException("A different answer is already accepted for this question");
        }

        answerToAccept.setAccepted(true);
        question.setSolved(true);
    }

    @Transactional
    public void addView(Long questionId, String userEmail) {
        appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        Question question = questionRepository.findQuestionWithAllAnswers(questionId)
                .orElseThrow(() -> new ResourceNotFound("Question not found"));

        question.setViews(question.getViews() + 1);
    }
}
