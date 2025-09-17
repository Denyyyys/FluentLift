package com.denyyyys.fluentLift.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denyyyys.fluentLift.exceptions.ResourceNotFound;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.CourseCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CourseResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.course.Course;
import com.denyyyys.fluentLift.model.postgres.mapper.CourseMapper;
import com.denyyyys.fluentLift.repo.postgres.AppUserRepository;
import com.denyyyys.fluentLift.repo.postgres.CourseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
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
        List<Course> courses = courseRepository.findAllByCreatorEmail(creatorEmail);
        List<CourseResponseDto> coursesResponse = courses.stream().map(course -> CourseMapper.toResponseDto(course))
                .toList();

        return coursesResponse;
    }

}
