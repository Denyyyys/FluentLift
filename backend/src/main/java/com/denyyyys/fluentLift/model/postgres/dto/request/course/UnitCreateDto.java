package com.denyyyys.fluentLift.model.postgres.dto.request.course;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class UnitCreateDto {
    @NotBlank
    private String title;

    private String overview;

    @NotNull
    private Integer unitNumber;

    @NotEmpty
    @Valid
    private List<LessonCreateDto> lessons;
}