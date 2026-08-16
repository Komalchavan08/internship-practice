package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.dto.ChangePasswordRequest;
import com.example.StudentManagementAPI.dto.ProfileResponse;
import com.example.StudentManagementAPI.dto.ProfileUpdateRequest;
import com.example.StudentManagementAPI.entity.User;
import com.example.StudentManagementAPI.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUp(@Valid @RequestBody User user) {

        String message = service.register(user);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/admin-exists")
    public ResponseEntity<Map<String, Object>> adminExists() {

        Map<String, Object> response = new HashMap<>();
        response.put("adminExists", service.adminExists());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {

        String message = service.login(user.getEmail(), user.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("message", message);

        if (message.equals("Login Successful")) {

            response.put("status", HttpStatus.OK.value());
            response.put("role", service.getRoleNameByEmail(user.getEmail()));
            response.put("userId", service.getUserId(user.getEmail()));

            return ResponseEntity.ok(response);

        } else if (message.equals("Incorrect Password")) {

            response.put("status", HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } else {

            response.put("status", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {

        String message = service.logout();

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    // View Profile
    @GetMapping("/profile/{id}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable int id) {
        return ResponseEntity.ok(service.getProfile(id));
    }

    // Update Profile
    @PutMapping("/profile/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable int id,
                                                         @Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(service.updateProfile(id, request));
    }

    // Upload Profile Photo
    @PostMapping("/profile/{id}/photo")
    public ResponseEntity<ProfileResponse> uploadPhoto(@PathVariable int id,
                                                       @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(service.uploadProfilePhoto(id, file));
    }

    // Change Password
    @PutMapping("/profile/{id}/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@PathVariable int id,
                                                              @Valid @RequestBody ChangePasswordRequest request) {

        service.changePassword(id, request.getCurrentPassword(), request.getNewPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Password changed successfully");

        return ResponseEntity.ok(response);
    }
}