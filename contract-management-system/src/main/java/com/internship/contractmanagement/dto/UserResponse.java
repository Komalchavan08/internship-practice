package com.internship.contractmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Shape of the JSON we send BACK to the client.
 * Deliberately has NO password field - even though the User entity has one,
 * we never want a password (even hashed) leaking out through an API response.
 * This is the single biggest reason DTOs exist.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private LocalDateTime createdAt;
    private Set<String> roles; // just role names, e.g. ["ADMIN", "EDITOR"]
}
