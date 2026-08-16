package com.example.StudentManagementAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Fields a logged-in user (Admin or Student) may update about their own
 * profile. Email is intentionally excluded — it's the login identifier and
 * isn't editable here.
 */
public class ProfileUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Mobile is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Enter a valid 10-digit mobile number")
    private String mobile;

    @NotBlank(message = "Date of birth is required")
    private String dob;

    @NotBlank(message = "Address is required")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}