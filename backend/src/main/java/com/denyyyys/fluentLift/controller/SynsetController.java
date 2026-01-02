package com.denyyyys.fluentLift.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.denyyyys.fluentLift.model.neo4j.entity.Synset;
import com.denyyyys.fluentLift.service.SynsetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/synsets")
@RequiredArgsConstructor
public class SynsetController {
    private final SynsetService synsetService;

    @GetMapping("/search")
    public ResponseEntity<Optional<Synset>> findByLemma(@RequestParam String lemma) {
        return ResponseEntity.ok(synsetService.findByLemma(lemma));
    }
}
