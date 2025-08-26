package com.denyyyys.fluentLift.service;

import org.springframework.stereotype.Service;

import com.denyyyys.fluentLift.repo.postgres.CourseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

}
