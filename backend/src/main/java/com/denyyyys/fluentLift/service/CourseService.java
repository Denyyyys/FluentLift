package com.denyyyys.fluentLift.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denyyyys.fluentLift.exceptions.ForbiddenActionException;
import com.denyyyys.fluentLift.exceptions.ResourceAlreadyExistsException;
import com.denyyyys.fluentLift.exceptions.ResourceNotFound;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.CourseCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CourseResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.UserCourseEnrollmentResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.course.Course;
import com.denyyyys.fluentLift.model.postgres.entity.course.UserCourseEnrollment;
import com.denyyyys.fluentLift.model.postgres.mapper.CourseMapper;
import com.denyyyys.fluentLift.model.postgres.mapper.UserCourseEnrollmentMapper;
import com.denyyyys.fluentLift.repo.postgres.AppUserRepository;
import com.denyyyys.fluentLift.repo.postgres.CourseRepository;
import com.denyyyys.fluentLift.repo.postgres.UserCourseEnrollmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserCourseEnrollmentRepository enrollmentRepository;
    private final AppUserRepository appUserRepository;

    @Transactional
    public Course createCourse(CourseCreateDto dto, String creatorEmail) {
        Course course = CourseMapper.toEntity(dto);
        AppUser creator = appUserRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new ResourceNotFound("Creator not found"));
        course.setCreator(creator);

        courseRepository.save(course);
        return course;
    }

    public List<CourseResponseDto> getCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponseDto> coursesResponse = courses.stream().map(course -> CourseMapper.toResponseDto(course))
                .toList();

        return coursesResponse;
    }

    public List<CourseResponseDto> getCreatedByMeCourses(String creatorEmail) {
        List<Course> courses = courseRepository.findAllByCreator_Email(creatorEmail);
        List<CourseResponseDto> coursesResponse = courses.stream().map(course -> CourseMapper.toResponseDto(course))
                .toList();

        return coursesResponse;
    }

    public UserCourseEnrollmentResponseDto enroll(String userEmail, Long courseId) {
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFound("Course not found"));

        if (user.getId().equals(course.getCreator().getId())) {
            throw new ForbiddenActionException("Course creator cannot enroll in their own course.");
        }

        Optional<UserCourseEnrollment> enrollmentFromRepo = enrollmentRepository.findByCourseAndUser(course, user);

        if (enrollmentFromRepo.isPresent()) {
            throw new ResourceAlreadyExistsException("Enrollment for provided user already exists");
        }

        UserCourseEnrollment enrollment = new UserCourseEnrollment();

        enrollment.setCourse(course);
        enrollment.setUser(user);
        enrollment = enrollmentRepository.save(enrollment);

        return UserCourseEnrollmentMapper.toResponseDto(enrollment, "enrolled");
    }

    public List<CourseResponseDto> getEnrolledCourses(String userEmail) {
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        List<Course> courses = enrollmentRepository.findCoursesByUserId(user.getId());

        List<CourseResponseDto> coursesResponse = courses.stream().map(course -> CourseMapper.toResponseDto(course))
                .toList();

        return coursesResponse;
    }

}
