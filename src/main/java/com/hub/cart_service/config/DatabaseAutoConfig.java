package com.hub.cart_service.config;

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
@EntityScan("com.hub.cart_service.model")
@EnableJpaRepositories("com.hub.cart_service.repository")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DatabaseAutoConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null)
                return Optional.of("system");

            return Optional.of(authentication.getName());
        };
    }

}
