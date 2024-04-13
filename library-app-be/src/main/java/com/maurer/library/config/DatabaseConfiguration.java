package com.maurer.library.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

@Configuration
public class DatabaseConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Bean
    public FlywayMigrationStrategy cleanMigrationStrategy() {
        return library -> {
            library.repair();
            library.migrate();
        };
    }
}