package com.denyyyys.fluentLift.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denyyyys.fluentLift.service.SynsetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/synsets")
@RequiredArgsConstructor
public class SynsetController {
    private final SynsetService synsetService;

}
