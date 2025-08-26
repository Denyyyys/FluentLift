package com.denyyyys.fluentLift.repo.postgres;

import org.springframework.data.jpa.repository.JpaRepository;

import com.denyyyys.fluentLift.model.postgres.entity.course.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}