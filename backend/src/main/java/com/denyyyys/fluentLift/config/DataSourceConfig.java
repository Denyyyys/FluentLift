package com.denyyyys.fluentLift.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    // uncomment if app is started locally (not usign docker compose) and local .env
    // (resources/.env) file is
    // @Bean
    // public DataSource dataSource() {
    // Dotenv dotenv = Dotenv.load();
    // return DataSourceBuilder.create()
    // .url(dotenv.get("SQL_DB_URL"))
    // .username(dotenv.get("SQL_DB_USERNAME"))
    // .password(dotenv.get("SQL_DB_PASSWORD"))
    // .driverClassName("org.postgresql.Driver")
    // .build();
    // }
}
