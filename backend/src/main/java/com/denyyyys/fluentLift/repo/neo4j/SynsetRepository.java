package com.denyyyys.fluentLift.repo.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.denyyyys.fluentLift.model.neo4j.entity.Synset;

public interface SynsetRepository extends Neo4jRepository<Synset, String> {

}
