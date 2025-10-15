package com.denyyyys.fluentLift.model.postgres.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClozeBlockUserProgressWithAnswersResponseDto {
    private Long id;

    private Long clozeBlockAnswerId;

    private String userInput;
}
