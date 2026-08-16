package com.example.StudentManagementAPI.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Reads the "X-User-Email" and "X-User-Role" headers the frontend sends on
 * requests and stores them for this request only — used by JPA auditing
 * (createdBy/updatedBy) and by role-gated endpoints like Activity Logs.
 * Falls back to "SYSTEM" / no role when the headers are missing.
 */
@Component
public class AuditHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String actingUser = request.getHeader("X-User-Email");
        String actingRole = request.getHeader("X-User-Role");

        CurrentUserContext.set((actingUser == null || actingUser.isBlank()) ? "SYSTEM" : actingUser);
        CurrentUserContext.setRole(actingRole);

        try {
            filterChain.doFilter(request, response);
        } finally {
            CurrentUserContext.clear();
        }
    }
}