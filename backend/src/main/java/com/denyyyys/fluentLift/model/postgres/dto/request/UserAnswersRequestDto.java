package com.denyyyys.fluentLift.model.postgres.dto.request;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswersRequestDto {
    private List<ClozeBlockUserAnswerRequestDto> clozeAnswers = new ArrayList<>();
    private List<Long> multipleChoiceUserSelectedOptionIds = new ArrayList<>();

}
