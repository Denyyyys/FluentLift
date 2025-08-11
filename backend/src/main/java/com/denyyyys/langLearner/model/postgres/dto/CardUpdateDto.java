package com.denyyyys.langLearner.model.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardUpdateDto {
    private String frontText;

    private String backText;

    private Boolean isArchived;
}
