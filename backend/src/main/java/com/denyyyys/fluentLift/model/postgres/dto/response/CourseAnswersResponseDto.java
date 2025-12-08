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
public class CourseAnswersResponseDto {
    private Long courseId;
    private Long userId;

    private List<UnitAnswersResponseDto> unitAnswers = new ArrayList<>();
}
