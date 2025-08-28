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
public class CourseCreateDto {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private List<String> goals;

    @NotNull
    private String prerequisiteLevel;

    @NotNull
    private String outcomeLevel;

    @NotBlank
    private String baseLanguage;

    @NotBlank
    private String targetLanguage;

    @NotEmpty
    @Valid
    private List<UnitCreateDto> units;
}
