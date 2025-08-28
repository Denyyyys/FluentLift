package com.denyyyys.fluentLift.model.postgres.dto.request.course;

import java.util.List;

import com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock.LessonBlockCreateDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonCreateDto {

    private String title;

    @NotNull
    private Integer lessonNumber;

    @NotEmpty
    @Valid
    private List<LessonBlockCreateDto> blocks;

}
