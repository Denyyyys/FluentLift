package com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock;

import com.denyyyys.fluentLift.model.postgres.enums.TextBlockType;

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
public class TextBlockCreateDto extends LessonBlockCreateDto {
    @NotBlank
    private String text;

    @NotNull
    private TextBlockType textBlockType;

    @Override
    public String getType() {
        return "text";
    }

}
