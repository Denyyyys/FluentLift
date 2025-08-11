package com.denyyyys.langLearner.model.postgres.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeckCreateDto {
    @NotBlank(message = "Name of deck cannot be empty")
    private String name;

    private String targetLanguage;
    private String learningLanguage;

    private List<CardCreateDto> cards;
}
