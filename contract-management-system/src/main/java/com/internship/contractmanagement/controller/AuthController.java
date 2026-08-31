package com.internship.contractmanagement.controller;

import com.internship.contractmanagement.config.JwtUtil;
import com.internship.contractmanagement.dto.LoginRequest;
import com.internship.contractmanagement.dto.LoginResponse;
import com.internship.contractmanagement.entity.Role;
import com.internship.contractmanagement.entity.User;
import com.internship.contractmanagement.exception.ResourceNotFoundException;
import com.internship.contractmanagement.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // This is the ACTUAL password check. Internally, Spring Security
            // calls CustomUserDetailsService to load the user, then compares
            // the submitted password against the stored BCrypt hash.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Deliberately vague message - never reveal whether it was the
            // email or the password that was wrong (a real security practice,
            // stops attackers from confirming which emails exist in our system)
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), roleNames);

        LoginResponse response = new LoginResponse(
                token, user.getId(), user.getFullName(), user.getEmail(), roleNames
        );

        return ResponseEntity.ok(response);
    }
}