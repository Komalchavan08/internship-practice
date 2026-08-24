package com.internship.contractmanagement.controller;

import com.internship.contractmanagement.dto.UserRequest;
import com.internship.contractmanagement.dto.UserResponse;
import com.internship.contractmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API endpoints for User CRUD.
 * @RestController = @Controller + @ResponseBody -> every method's return
 * value gets automatically converted to JSON in the HTTP response.
 */
@RestController
@RequestMapping("/api/users") // every endpoint below starts with /api/users
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService; // Spring injects the Service automatically
    }

    // POST /api/users  -> Create a new user
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        // @RequestBody: convert incoming JSON -> UserRequest object
        // @Valid: run the @NotBlank/@Email checks we put on UserRequest
        UserResponse created = userService.createUser(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED); // 201
    }

    // GET /api/users -> List all users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers()); // 200
    }

    // GET /api/users/5 -> Get one user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        // @PathVariable: pulls the {id} out of the URL and passes it in as a parameter
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // PUT /api/users/5 -> Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // DELETE /api/users/5 -> Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204, no response body
    }
}
