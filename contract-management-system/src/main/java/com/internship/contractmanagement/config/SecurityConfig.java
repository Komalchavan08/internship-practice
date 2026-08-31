package com.internship.contractmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * REAL security configuration - replaces the earlier temporary "permit all"
 * config now that login and roles actually exist.
 *
 * Two big ideas:
 * 1. STATELESS sessions - because we use JWT, the server doesn't remember
 *    who's logged in between requests. Every request proves itself with
 *    its own token.
 * 2. Role-based URL rules - different endpoints require different roles.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Turns plain-text passwords into irreversible BCrypt hashes, and later
    // checks a plain-text password against a stored hash at login time.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring needs this bean to actually perform the "check email+password"
    // step during login - AuthController will call this.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // not needed for a stateless JSON API

                // STATELESS: no server-side session is created or used.
                // Every request must carry its own valid JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // ---------- PUBLIC: no login required ----------
                        .requestMatchers("/api/auth/**").permitAll()                // login endpoint itself
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/", "/login.html", "/dashboard.html", "/css/**", "/js/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users").permitAll() // registration

                        // ---------- ADMIN only ----------
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/roles/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/roles/**").hasRole("ADMIN")
                        // GET /api/roles is deliberately open to any logged-in user (see below) -
                        // reading the role list isn't sensitive, and someone needs to be able to
                        // look up role IDs before they can ever become an ADMIN in the first place

                        // ---------- EDITOR or ADMIN: create/modify contract content ----------
                        .requestMatchers(org.springframework.http.HttpMethod.POST,
                                "/api/contracts/**", "/api/versions/**", "/api/clauses/**",
                                "/api/documents/**", "/api/modifications/**").hasAnyRole("EDITOR", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                "/api/contracts/**", "/api/versions/**", "/api/clauses/**",
                                "/api/documents/**").hasAnyRole("EDITOR", "ADMIN")

                        // ---------- APPROVER or ADMIN: the approval workflow ----------
                        .requestMatchers("/api/approvals/**").hasAnyRole("APPROVER", "ADMIN")

                        // ---------- Any logged-in user: read-only GETs, audit log creation ----------
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/**").authenticated()
                        .requestMatchers("/api/audit-logs/**").authenticated()

                        // Everything else must be authenticated by default
                        .anyRequest().authenticated()
                )

                // Insert our JWT check BEFORE Spring's default login-form filter,
                // since we're replacing that mechanism entirely with tokens
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}