package com.denyyyys.fluentLift.model.postgres.mapper;

import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.AnswerCreateRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers.QuestionCreateRequestDto;
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
}
