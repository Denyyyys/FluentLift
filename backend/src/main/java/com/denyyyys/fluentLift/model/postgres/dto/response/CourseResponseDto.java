package com.denyyyys.fluentLift.model.postgres.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.denyyyys.fluentLift.model.postgres.entity.course.Unit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDto {
    private Long id;

    private CourseCreatorDto creator;

    private String title;

    private String description;

    private List<String> goals = new ArrayList<>();

    private String prerequisiteLevel;

    private String outcomeLevel;

    private String baseLanguage;

    private String targetLanguage;

    private List<Unit> units = new ArrayList<>();
}
