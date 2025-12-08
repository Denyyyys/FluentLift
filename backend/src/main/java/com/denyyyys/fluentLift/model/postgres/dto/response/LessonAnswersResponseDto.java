package com.denyyyys.fluentLift.model.postgres.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonAnswersResponseDto {
    private Long lessonId;
    private List<ClozeBlockUserAnswersResponseDto> clozeAnswers = new ArrayList<>();

    private List<Long> userSelectedMcosIds = new ArrayList<>();
}
