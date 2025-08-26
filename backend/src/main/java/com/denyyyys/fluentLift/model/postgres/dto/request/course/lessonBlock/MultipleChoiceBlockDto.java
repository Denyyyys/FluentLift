package com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceBlockDto extends LessonBlockCreateDto {
    @NotBlank
    private String question;

    @NotEmpty
    private List<MultipleChoiceOptionDto> choices;

    @Override
    public String getType() {
        return "multipleChoice";
    }

}
