package com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPageResponseDto {
    private List<QuestionResponseDto> questions;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
