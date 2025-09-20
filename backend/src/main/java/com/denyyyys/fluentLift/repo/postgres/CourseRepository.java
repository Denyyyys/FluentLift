package com.denyyyys.fluentLift.repo.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.denyyyys.fluentLift.model.postgres.entity.course.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByCreator_Email(String email);
}