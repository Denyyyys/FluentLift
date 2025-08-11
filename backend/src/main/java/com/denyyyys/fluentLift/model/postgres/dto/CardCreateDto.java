package com.denyyyys.fluentLift.model.postgres.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardCreateDto {
    @NotBlank(message = "Front text cannot be empty")
    private String frontText;

    @NotBlank(message = "Back text cannot be empty")
    private String backText;

}
