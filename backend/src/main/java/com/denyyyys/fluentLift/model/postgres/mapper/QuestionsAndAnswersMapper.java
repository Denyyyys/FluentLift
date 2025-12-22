package com.denyyyys.fluentLift.model.postgres.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.AnswerCreateRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.QuestionCreateRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers.AnswerThreadDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers.QuestionResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers.QuestionWithAnswersDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.questionsAndAnswers.Answer;
import com.denyyyys.fluentLift.model.postgres.entity.questionsAndAnswers.Question;

public class QuestionsAndAnswersMapper {
    public static Question toEntity(QuestionCreateRequestDto questionDto, AppUser author) {
        Question question = new Question();

        question.setAuthor(author);

        question.setTitle(questionDto.getTitle());
        question.setContent(questionDto.getContent());
        question.setTargetLanguage(questionDto.getTargetLanguage());
        question.setBaseLanguage(questionDto.getBaseLanguage());
        question.setTags(questionDto.getTags());

        return question;
    }

    public static Answer toEntity(AnswerCreateRequestDto answerDto, Long questionId, AppUser author) {

        Answer answer = new Answer();

        Question question = new Question();
        question.setId(questionId);

        answer.setAuthor(author);
        answer.setContent(answerDto.getContent());
        answer.setQuestion(question);

        if (answerDto.getParentAnswerId() != null) {
            Answer parentAnswer = new Answer();
            parentAnswer.setId(answerDto.getParentAnswerId());
            answer.setParent(parentAnswer);
        }

        return answer;
    }

    public static QuestionResponseDto toDto(Question question) {
        QuestionResponseDto dto = new QuestionResponseDto();

        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setContent(question.getContent());
        dto.setTargetLanguage(question.getTargetLanguage());
        dto.setBaseLanguage(question.getBaseLanguage());
        dto.setAuthor(AppUserMapper.toDto(question.getAuthor()));
        dto.setViews(question.getViews());
        dto.setSolved(question.isSolved());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());
        dto.setUpvotes(question.getUpvotes());
        dto.setDownvotes(question.getDownvotes());
        dto.setTags(question.getTags());

        return dto;
    }

    public static AnswerThreadDto toDto(Answer answer) {
        AnswerThreadDto dto = new AnswerThreadDto();

        dto.setId(answer.getId());
        dto.setContent(answer.getContent());
        dto.setAuthor(AppUserMapper.toDto(answer.getAuthor()));
        dto.setUpvotes(answer.getUpvotes());
        dto.setDownvotes(answer.getDownvotes());
        dto.setAccepted(answer.isAccepted());
        dto.setCreatedAt(answer.getCreatedAt());
        dto.setUpdatedAt(answer.getUpdatedAt());

        return dto;
    }

    public static QuestionWithAnswersDto toQuestionWithAnswersDto(Question question) {

        QuestionWithAnswersDto dto = new QuestionWithAnswersDto();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setContent(question.getContent());
        dto.setTargetLanguage(question.getTargetLanguage());
        dto.setBaseLanguage(question.getBaseLanguage());
        dto.setAuthor(AppUserMapper.toDto(question.getAuthor()));
        dto.setViews(question.getViews());
        dto.setSolved(question.isSolved());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());
        dto.setUpvotes(question.getUpvotes());
        dto.setDownvotes(question.getDownvotes());
        dto.setTags(question.getTags());

        List<Answer> answers = question.getAnswers();

        Map<Long, AnswerThreadDto> answerMap = new HashMap<>();

        for (Answer answer : answers) {
            answerMap.put(answer.getId(), QuestionsAndAnswersMapper.toDto(answer));
        }

        List<AnswerThreadDto> rootAnswers = new ArrayList<>();

        for (Answer answer : answers) {
            AnswerThreadDto currentDto = answerMap.get(answer.getId());

            if (answer.getParent() == null) {
                rootAnswers.add(currentDto);
            } else {
                AnswerThreadDto parentDto = answerMap.get(answer.getParent().getId());

                if (parentDto != null) {
                    parentDto.getReplies().add(currentDto);
                }
            }
        }

        dto.setAnswers(rootAnswers);
        return dto;
    }
}
