package com.example.StudentManagementAPI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)

    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){

        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {

            errors.put(error.getField(), error.getDefaultMessage());

        });

        return errors;

    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgumentException(IllegalArgumentException ex){

        Map<String, Object> response = new HashMap<>();

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());

        return response;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleAccessDeniedException(AccessDeniedException ex){

        Map<String, Object> response = new HashMap<>();

        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("message", ex.getMessage());

        return response;
    }

    @ExceptionHandler(AdminAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleAdminAlreadyExistsException(AdminAlreadyExistsException ex){

        Map<String, Object> response = new HashMap<>();

        response.put("status", HttpStatus.CONFLICT.value());
        response.put("message", ex.getMessage());

        return response;
    }

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleStudentNotFoundException(StudentNotFoundException ex){

        Map<String, Object> response = new HashMap<>();

        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());

        return response;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleUserNotFoundException(UserNotFoundException ex){

        Map<String, Object> response = new HashMap<>();

        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());

        return response;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){

        Map<String, Object> response = new HashMap<>();

        response.put("status", HttpStatus.CONFLICT.value());
        response.put("message", ex.getMessage());

        return response;
    }
}