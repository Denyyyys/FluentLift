package com.denyyyys.fluentLift.model.postgres.dto.request.questionsAndAnswers;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerCreateRequestDto {
    @NotBlank(message = "Content is required")
    private String content;

    private Long parentAnswerId;
}
