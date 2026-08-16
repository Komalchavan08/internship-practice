package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.entity.Role;
import com.example.StudentManagementAPI.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Finds a role by name, or creates it if this is the first time it's
     * been used. The very first role ever created gets id 1 automatically
     * (normal DB auto-increment) — nothing here hardcodes an id.
     */
    public Role getOrCreateRole(String name) {

        String normalized = (name == null || name.isBlank())
                ? "STUDENT"
                : name.trim().toUpperCase();

        return roleRepository.findByNameIgnoreCase(normalized)
                .orElseGet(() -> roleRepository.save(new Role(normalized)));
    }
}