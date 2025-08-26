package com.hub.course_service.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EntityScan("com.hub.course_service.model")
@EnableJpaRepositories("com.hub.course_service.repository")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DatabaseAutoConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null)
                return Optional.of("");
            return Optional.of(auth.getName()); // Returns the name of this principal.
        };
    }

}

/*
        -- This class used for keeping track of who created or changed an entity
        and when the change happened (Auditing) - implementation by AuditorAware<T>

        -- @EnableJpaAuditing: Enable Spring Data JPA Auditing to autopopulate
        @CreatedBy / @LastModifiedBy information based on auditorAware method

        -- @EntityScan: Tell Spring where entities are located,
        so that JPA can map the corresponding tables.

        -- @EnableJpaRepositories: Tell Spring where repository interface are located
        to create proxy JPA

        -- The principal is the currently logged-in user
        Principal.getName()      Returns the name of this principal (username).
*/