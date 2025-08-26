package com.denyyyys.fluentLift.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denyyyys.fluentLift.model.postgres.dto.request.course.CourseCreateDto;
import com.denyyyys.fluentLift.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public String createCourse(@AuthenticationPrincipal UserDetails user,
            @Validated @RequestBody CourseCreateDto courseDto) {

        return "yoo" + courseDto.toString() + user.getUsername();
    }
}
