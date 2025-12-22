package com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers;

import java.time.LocalDateTime;

import com.denyyyys.fluentLift.model.postgres.dto.response.AppUserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponseDto {
    private Long id;
    private String content;
    private AppUserResponseDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int upvotes;
    private int downvotes;
    private boolean accepted;

}
