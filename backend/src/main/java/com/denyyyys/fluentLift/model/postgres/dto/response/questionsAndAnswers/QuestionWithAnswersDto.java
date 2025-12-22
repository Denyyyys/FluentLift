package com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.denyyyys.fluentLift.model.postgres.dto.response.AppUserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionWithAnswersDto {
    private Long id;
    private String title;
    private String content;
    private String targetLanguage;
    private String baseLanguage;
    private AppUserResponseDto author;

    private int views;
    private boolean solved;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer upvotes = 0;
    private Integer downvotes = 0;
    private List<String> tags;

    private List<AnswerThreadDto> answers = new ArrayList<>();
}
