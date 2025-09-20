package com.denyyyys.fluentLift.repo.postgres;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.course.Course;
import com.denyyyys.fluentLift.model.postgres.entity.course.UserCourseEnrollment;

public interface UserCourseEnrollmentRepository extends JpaRepository<UserCourseEnrollment, Long> {
    Optional<UserCourseEnrollment> findByCourseAndUser(Course course, AppUser user);
}
