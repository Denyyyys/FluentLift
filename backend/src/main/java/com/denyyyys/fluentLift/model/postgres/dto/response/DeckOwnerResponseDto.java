package com.denyyyys.fluentLift.model.postgres.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeckOwnerResponseDto {
    private Long id;
    private String name;
    private DeckCreatorDto creator;
    private boolean archived;
    private String targetLanguage;
    private String learningLanguage;
    private boolean isPublic;

    private List<CardOwnerResponseDto> cards;

}
