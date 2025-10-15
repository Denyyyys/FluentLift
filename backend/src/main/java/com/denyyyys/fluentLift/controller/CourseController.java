package com.denyyyys.fluentLift.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denyyyys.fluentLift.model.postgres.dto.request.course.CourseCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CourseProgressWithAnswersResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CourseResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.UserCourseEnrollmentResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.course.Course;
import com.denyyyys.fluentLift.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/courses")
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
    public ResponseEntity<List<CourseResponseDto>> getCourses() {
        return ResponseEntity.ok().body(courseService.getCourses());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDto> getCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok().body(courseService.getCourse(courseId));
    }

    @GetMapping("/me/created")
    public ResponseEntity<List<CourseResponseDto>> getCreatedByMeCourses(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok().body(courseService.getCreatedByMeCourses(user.getUsername()));
    }

    @PostMapping("/{courseId}/users")
    public ResponseEntity<UserCourseEnrollmentResponseDto> enroll(@AuthenticationPrincipal UserDetails user,
            @PathVariable Long courseId) {
        UserCourseEnrollmentResponseDto responseDto = courseService.enroll(user.getUsername(), courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{courseId}/enrollment")
    public ResponseEntity<UserCourseEnrollmentResponseDto> getMyEnrollment(@AuthenticationPrincipal UserDetails user,
            @PathVariable Long courseId) {
        return ResponseEntity.ok().body(courseService.userIsEnrolled(user.getUsername(), courseId));

    }

    @GetMapping("/me/enrolled")
    public ResponseEntity<List<CourseResponseDto>> getEnrolledCourses(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok().body(courseService.getEnrolledCourses(user.getUsername()));
    }

    @GetMapping("/{courseId}/progress-with-answers")
    public ResponseEntity<CourseProgressWithAnswersResponseDto> getProgressWithAnswers(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long courseId) {

        CourseProgressWithAnswersResponseDto progress = courseService.getProgressWithAnswers(user.getUsername(),
                courseId);
        return ResponseEntity.ok().body(progress);
    }

}
