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
public class DeckOwnerPageResponseDto {

    private List<DeckOwnerResponseDto> decks;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
