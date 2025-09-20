package com.denyyyys.fluentLift.model.postgres.mapper;

import com.denyyyys.fluentLift.model.postgres.dto.response.UserCourseEnrollmentResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.course.UserCourseEnrollment;

public class UserCourseEnrollmentMapper {
    public static UserCourseEnrollmentResponseDto toResponseDto(UserCourseEnrollment entity, String status) {
        return new UserCourseEnrollmentResponseDto(
                entity.getUser().getId(),
                entity.getCourse().getId(),
                entity.getEnrolledAt(),
                status);
    }
}
