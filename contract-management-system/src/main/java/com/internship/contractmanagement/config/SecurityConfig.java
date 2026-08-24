package com.internship.contractmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * TEMPORARY security configuration.
 *
 * spring-boot-starter-security auto-locks every endpoint behind a login
 * by default. Since real authentication (User login, JWT, roles) is a
 * SEPARATE upcoming task, this class disables that default lock for now,
 * so we can freely test the CRUD APIs and Swagger UI while building them.
 *
 * TODO: Replace this entire class in the Authentication task with real
 * login rules (e.g. only ADMIN can DELETE, only APPROVER can approve, etc.)
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF protection is for browser-form-based apps; our API is
                // called with JSON (Postman/Swagger/frontend), so we disable it.
                // We WILL reconsider this properly once JWT auth is added.
                .csrf(csrf -> csrf.disable())

                // Allow every request through, no login required - TEMPORARY
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}
