package com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock;

import java.util.List;

import com.denyyyys.fluentLift.validators.ValidClozeBlock;

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
@ValidClozeBlock
public class ClozeBlockCreateDto extends LessonBlockCreateDto {
    @NotBlank
    private String question;

    @NotBlank
    private String template;

    @NotEmpty
    private List<ClozeBlockAnswerDto> answers;

    @Override
    public String getType() {
        return "cloze";
    }

}
