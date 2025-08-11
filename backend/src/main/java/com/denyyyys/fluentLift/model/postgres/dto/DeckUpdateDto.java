package com.denyyyys.fluentLift.model.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeckUpdateDto {
    private String name;

    private String targetLanguage;
    private String learningLanguage;
}
