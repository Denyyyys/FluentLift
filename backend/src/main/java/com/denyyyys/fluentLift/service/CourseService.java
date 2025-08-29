package com.denyyyys.fluentLift.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denyyyys.fluentLift.exceptions.ResourceNotFound;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.CourseCreateDto;
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

    public List<Course> getCourses() {
        return courseRepository.findAll();
    }
}
