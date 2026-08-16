package com.example.StudentManagementAPI.dto;

import jakarta.validation.constraints.*;


public class StudentRequest {

    @NotBlank(message = "Student Name is required")
    private String studentName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 5 characters")
    private String password;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "City is required")
    private String city;

    @Min(value = 18, message = "Age should be at least 18")
    @Max(value = 60, message = "Age should not exceed 60")
    private int age;

    @NotBlank(message = "Course is required")
    private String course;

    @NotBlank(message = "Mobile is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Enter a valid 10-digit mobile number")
    private String mobile;

    @NotBlank(message = "Date of birth is required")
    private String dob;

    @NotBlank(message = "Address is required")
    private String address;

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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}