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
public class CourseProgressWithAnswersResponseDto {
    private Long courseId;
    private Long userId;

    private Integer progress;

    private List<UnitProgressWithAnswersResponseDto> unitProgresses = new ArrayList<>();
}
