package com.example.StudentManagementAPI.exception;

public class AdminAlreadyExistsException extends RuntimeException {

    public AdminAlreadyExistsException(String message) {
        super(message);
    }
}