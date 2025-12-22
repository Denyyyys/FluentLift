package com.denyyyys.fluentLift.model.postgres.dto.response.questionsAndAnswers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.denyyyys.fluentLift.model.postgres.dto.response.AppUserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerThreadDto {
    private Long id;
    private String content;

    private AppUserResponseDto author;

    private int upvotes;
    private int downvotes;
    private boolean accepted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // replies (thread)
    private List<AnswerThreadDto> replies = new ArrayList<>();
}
