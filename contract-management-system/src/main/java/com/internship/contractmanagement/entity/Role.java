package com.internship.contractmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a role a User can hold in the system.
 * Examples: ADMIN, EDITOR, APPROVER, VIEWER
 *
 * Kept as its own table (instead of a plain string on User) so roles
 * can be managed independently and a user can hold more than one.
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. "ADMIN", "EDITOR", "APPROVER", "VIEWER"
    @Column(nullable = false, unique = true, length = 50)
    private String name;
}
