package com.denyyyys.fluentLift.repo.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.course.Course;
import com.denyyyys.fluentLift.model.postgres.entity.course.UserCourseEnrollment;

public interface UserCourseEnrollmentRepository extends JpaRepository<UserCourseEnrollment, Long> {
    Optional<UserCourseEnrollment> findByCourseAndUser(Course course, AppUser user);

    List<UserCourseEnrollment> findAllByUser(AppUser user);

    @Query("SELECT e.course FROM UserCourseEnrollment e WHERE e.user.id = :userId")
    List<Course> findCoursesByUserId(@Param("userId") Long userId);
}
