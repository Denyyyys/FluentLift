package com.denyyyys.fluentLift.model.postgres.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseEnrollmentResponseDto {
    private Long userId;
    private Long courseId;
    private LocalDateTime enrolledAt;
    private String status;
}
