package com.internship.contractmanagement.exception;

/**
 * Thrown whenever we look up something by ID and it doesn't exist,
 * e.g. GET /api/users/999 when no user with id=999 exists.
 * Caught globally by GlobalExceptionHandler and converted into a clean 404 response.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message); // just passes the message up to the parent RuntimeException
    }
}
