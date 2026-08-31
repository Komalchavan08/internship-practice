package com.internship.contractmanagement.config;

import com.internship.contractmanagement.entity.Role;
import com.internship.contractmanagement.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Runs ONCE automatically every time the app starts up (CommandLineRunner
 * is a Spring Boot hook for exactly that). Makes sure the 4 standard roles
 * always exist in the database - WITHOUT anyone needing to create them
 * through the API first.
 *
 * Why this matters: POST /api/roles requires the ADMIN role to use. But
 * nobody can HAVE the ADMIN role until the "ADMIN" Role row exists to be
 * assigned. Without this seeder, that's a deadlock nobody could escape.
 */
@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        List<String> defaultRoles = List.of("ADMIN", "EDITOR", "APPROVER", "VIEWER");

        for (String roleName : defaultRoles) {
            boolean alreadyExists = roleRepository.findAll().stream()
                    .anyMatch(r -> r.getName().equalsIgnoreCase(roleName));

            if (!alreadyExists) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
}