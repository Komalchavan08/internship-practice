package com.internship.contractmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

/**
 * Shape of the JSON the CLIENT sends us when creating/updating a user.
 * Notice: no "id" field (server assigns that), and roleIds instead of
 * full Role objects (client just sends role IDs, e.g. [1, 2]).
 */
@Data
public class UserRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    // Optional: IDs of roles to assign, e.g. [1, 2] for ADMIN + EDITOR
    private Set<Long> roleIds;
}
