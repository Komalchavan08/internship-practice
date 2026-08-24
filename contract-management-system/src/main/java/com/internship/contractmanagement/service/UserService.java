package com.internship.contractmanagement.service;

import com.internship.contractmanagement.dto.UserRequest;
import com.internship.contractmanagement.dto.UserResponse;
import com.internship.contractmanagement.entity.Role;
import com.internship.contractmanagement.entity.User;
import com.internship.contractmanagement.exception.ResourceNotFoundException;
import com.internship.contractmanagement.repository.RoleRepository;
import com.internship.contractmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * All the BUSINESS LOGIC for users lives here. The Controller stays thin
 * and just delegates to this class. This separation means: if the logic
 * needs to change, you edit ONE place, not every controller that touches users.
 */
@Service // tells Spring "create one instance of this and manage it for me"
public class UserService {

    // Constructor injection: Spring automatically supplies these when
    // it creates a UserService - we never write "new UserRepository()" ourselves.
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // ---------- CREATE ----------
    public UserResponse createUser(UserRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // NOTE: storing plain text for now on purpose - password hashing
        // (BCrypt) gets wired in properly during the Authentication task.
        user.setPassword(request.getPassword());

        // If the client sent roleIds, look up each Role and attach it
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            user.setRoles(resolveRoles(request.getRoleIds()));
        }

        User saved = userRepository.save(user); // INSERT happens here
        return mapToResponse(saved);
    }

    // ---------- READ (one) ----------
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponse(user);
    }

    // ---------- READ (all) ----------
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()                       // turn the List<User> into a stream
                .map(this::mapToResponse)       // convert each User -> UserResponse
                .collect(Collectors.toList());  // collect back into a List
    }

    // ---------- UPDATE ----------
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        if (request.getRoleIds() != null) {
            user.setRoles(resolveRoles(request.getRoleIds()));
        }

        User updated = userRepository.save(user); // UPDATE happens here (same id = update, not insert)
        return mapToResponse(updated);
    }

    // ---------- DELETE ----------
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // ---------- Helper: look up Role entities from a set of IDs ----------
    private Set<Role> resolveRoles(Set<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
            roles.add(role);
        }
        return roles;
    }

    // ---------- Helper: convert Entity -> Response DTO ----------
    private UserResponse mapToResponse(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt(),
                roleNames
        );
    }
}
