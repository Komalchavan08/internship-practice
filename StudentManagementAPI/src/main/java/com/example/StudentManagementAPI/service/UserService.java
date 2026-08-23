package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.dto.ForgotPasswordRequest;
import com.example.StudentManagementAPI.dto.ProfileResponse;
import com.example.StudentManagementAPI.dto.ProfileUpdateRequest;
import com.example.StudentManagementAPI.dto.ResetPasswordRequest;
import com.example.StudentManagementAPI.entity.Role;
import com.example.StudentManagementAPI.entity.User;
import com.example.StudentManagementAPI.exception.AdminAlreadyExistsException;
import com.example.StudentManagementAPI.exception.EmailAlreadyExistsException;
import com.example.StudentManagementAPI.exception.UserNotFoundException;
import com.example.StudentManagementAPI.repository.StudentRepository;
import com.example.StudentManagementAPI.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private EmailService emailService;

    // Registration — public "Registration" page. Defaults to ADMIN (matches
    // the form's default selection) and only ever allows one admin to be
    // created this way; the backend enforces this even if the UI hides the
    // Register link.
    public String register(User user) {

        if (repository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        String requestedRole = (user.getRoleName() == null || user.getRoleName().isBlank())
                ? "ADMIN"
                : user.getRoleName().trim().toUpperCase();

        if (!requestedRole.equals("ADMIN")) {
            throw new IllegalArgumentException(
                    "Only admin registration is allowed here. Students are added by an admin from the dashboard.");
        }

        if (repository.existsByRole_NameIgnoreCase("ADMIN")) {
            throw new AdminAlreadyExistsException(
                    "An admin is already registered. Please login instead.");
        }

        user.setRole(roleService.getOrCreateRole(requestedRole));
        user.setStatus("ACTIVE");

        User saved = repository.save(user);

        auditLogService.log("REGISTRATION", "User", String.valueOf(saved.getUserId()),
                "Admin registered: " + saved.getEmail());

        return "Registered Successfully";
    }

    // Used internally by StudentService when Admin adds a student — creates
    // the login/User side of a new student in one step (see StudentService).
    public User createUserWithRole(User user, String roleName) {

        if (repository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        Role role = roleService.getOrCreateRole(roleName);
        user.setRole(role);
        user.setStatus("ACTIVE");

        return repository.save(user);
    }

    // Whether an admin has already been registered — the frontend uses this
    // to decide whether to show or hide the Register link.
    public boolean adminExists() {
        return repository.existsByRole_NameIgnoreCase("ADMIN");
    }

    public String login(String email, String password) {

        Optional<User> user = repository.findByEmail(email);

        if (user.isPresent()) {

            if (user.get().getPassword().equals(password)) {
                auditLogService.log("LOGIN", "User", String.valueOf(user.get().getUserId()),
                        "User logged in: " + email);
                return "Login Successful";
            } else {
                return "Incorrect Password";
            }
        }

        return "Email Not Found";
    }

    public String getRoleNameByEmail(String email) {
        return repository.findByEmail(email)
                .map(existing -> existing.getRole() != null ? existing.getRole().getName() : "STUDENT")
                .orElse("STUDENT");
    }

    public Integer getUserId(String email) {
        return repository.findByEmail(email)
                .map(User::getUserId)
                .orElse(null);
    }

    public String logout() {
        auditLogService.log("LOGOUT", "User", null, "User logged out");
        return "Logout Successful";
    }

    // Plain save-through, used when Student edits update the linked User's
    // login info (name/email/password).
    public User updateUser(User user) {
        return repository.save(user);
    }

    public void deleteUser(User user) {
        repository.delete(user);
    }

    public ProfileResponse getProfile(int userId) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        ProfileResponse profile = new ProfileResponse();
        profile.setUserId(user.getUserId());
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        profile.setMobile(user.getMobile());
        profile.setDob(user.getDob());
        profile.setAddress(user.getAddress());
        profile.setStatus(user.getStatus());
        profile.setProfilePhoto(user.getProfilePhoto());
        profile.setRole(user.getRole() != null ? user.getRole().getName() : null);

        if ("STUDENT".equalsIgnoreCase(profile.getRole())) {
            studentRepository.findByUser_UserId(userId).ifPresent(student -> {
                profile.setDepartment(student.getDepartment());
                profile.setCourse(student.getCourse());
                profile.setCity(student.getCity());
                profile.setAge(student.getAge());
            });
        }

        return profile;
    }

    public ProfileResponse updateProfile(int userId, ProfileUpdateRequest request) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setName(request.getName());
        user.setMobile(request.getMobile());
        user.setDob(request.getDob());
        user.setAddress(request.getAddress());

        repository.save(user);

        auditLogService.log("UPDATE", "User", String.valueOf(userId),
                "Profile updated: " + user.getName());

        return getProfile(userId);
    }

    public void changePassword(int userId, String currentPassword, String newPassword) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (!user.getPassword().equals(currentPassword)) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        user.setPassword(newPassword);
        repository.save(user);

        auditLogService.log("PASSWORD_CHANGE", "User", String.valueOf(userId),
                "Password changed for: " + user.getEmail());
    }

    // Saves the uploaded photo to an external "uploads/profile-photos"
    // folder (not the classpath) so it survives app restarts/rebuilds, and
    // stores its public URL path on the user.
    public ProfileResponse uploadProfilePhoto(int userId, MultipartFile file) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        try {
            Path uploadDir = Paths.get("uploads", "profile-photos");
            Files.createDirectories(uploadDir);

            String originalName = file.getOriginalFilename();
            String extension = (originalName != null && originalName.contains("."))
                    ? originalName.substring(originalName.lastIndexOf("."))
                    : "";

            String filename = "user_" + userId + "_" + UUID.randomUUID() + extension;
            Path destination = uploadDir.resolve(filename);

            Files.copy(file.getInputStream(), destination);

            user.setProfilePhoto("/uploads/profile-photos/" + filename);
            repository.save(user);

            auditLogService.log("UPDATE", "User", String.valueOf(userId), "Profile photo updated");

            return getProfile(userId);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store profile photo: " + e.getMessage());
        }
    }

    // Forgot Password — generates a 6-digit OTP, stores it (10-minute
    // expiry), and emails it to the account's own address.
    public void forgotPassword(ForgotPasswordRequest request) {

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account found with this email."));

        String otp = generateOtp();

        user.setOtpCode(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10));

        repository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    // Reset Password — verifies the OTP matches and hasn't expired, then
    // sets the new password and invalidates the OTP so it can't be reused.
    public void resetPassword(ResetPasswordRequest request) {

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account found with this email."));

        if (user.getOtpCode() == null || user.getOtpExpiryTime() == null) {
            throw new IllegalArgumentException("No OTP was requested for this account.");
        }

        if (!user.getOtpCode().equals(request.getOtp())) {
            throw new IllegalArgumentException("Incorrect OTP.");
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiryTime())) {
            throw new IllegalArgumentException("This OTP has expired. Please request a new one.");
        }

        user.setPassword(request.getNewPassword());
        user.setOtpCode(null);
        user.setOtpExpiryTime(null);

        repository.save(user);

        auditLogService.log("PASSWORD_CHANGE", "User", String.valueOf(user.getUserId()),
                "Password reset via OTP for: " + user.getEmail());
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}