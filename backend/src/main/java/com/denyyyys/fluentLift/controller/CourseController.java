package com.denyyyys.fluentLift.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denyyyys.fluentLift.model.postgres.dto.request.course.CourseCreateDto;
import com.denyyyys.fluentLift.model.postgres.entity.course.Course;
import com.denyyyys.fluentLift.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Course> createCourse(@AuthenticationPrincipal UserDetails user,
            @Validated @RequestBody CourseCreateDto courseDto) {
        Course course = courseService.createCourse(courseDto, user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getCourses() {
        return ResponseEntity.ok().body(courseService.getCourses());
    }

}
