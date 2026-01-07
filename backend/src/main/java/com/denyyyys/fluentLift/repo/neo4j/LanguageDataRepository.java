package com.denyyyys.fluentLift.repo.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.denyyyys.fluentLift.model.neo4j.entity.LanguageData;

public interface LanguageDataRepository extends Neo4jRepository<LanguageData, String> {

}
