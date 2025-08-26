package com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClozeBlockAnswerDto {
    @NotBlank
    private String key;

    @NotBlank
    private String expected;

    @NotNull
    private Boolean caseSensitive;
}
