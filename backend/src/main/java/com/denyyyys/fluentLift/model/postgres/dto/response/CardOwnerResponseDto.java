package com.denyyyys.fluentLift.model.postgres.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardOwnerResponseDto {
    private Long id;

    private String frontText;

    private String backText;

    private boolean isArchived;

    private Instant nextReviewDate;

    private Instant firstReviewDate;

    private int intervalDays;

    private int consecutiveCorrect;
}
