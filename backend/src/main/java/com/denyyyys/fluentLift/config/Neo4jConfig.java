package com.denyyyys.fluentLift.config;

import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class Neo4jConfig {

    @Bean
    public PlatformTransactionManager neo4jTransactionManager(Driver driver) {
        return new Neo4jTransactionManager(driver);
    }

    @Bean
    public Neo4jClient neo4jClient(Driver driver) {
        return Neo4jClient.create(driver);
    }
    // uncomment if app is started locally (not usign docker compose) and local .env
    // (resources/.env) file is
    // creadentials
    // @Bean
    // public Driver neo4jDriver() {
    // Dotenv dotenv = Dotenv.load();
    // String uri = dotenv.get("NEO4J_URI");
    // String username = dotenv.get("NEO4J_USERNAME");
    // String password = dotenv.get("NEO4J_PASSWORD");
    // return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    // }
}