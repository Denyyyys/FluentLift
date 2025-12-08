package com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateRequestDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotBlank(message = "Target language is required")
    private String targetLanguage;

    @NotBlank(message = "Base language is required")
    private String baseLanguage;

    private List<String> tags = new ArrayList<>();

}
