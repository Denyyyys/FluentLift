package com.denyyyys.fluentLift.model.postgres.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardVisitorResponseDto {
    private Long id;

    private String frontText;

    private String backText;

    private boolean isArchived;

}
