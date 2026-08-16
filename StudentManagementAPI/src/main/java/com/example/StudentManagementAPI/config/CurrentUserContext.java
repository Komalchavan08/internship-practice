package com.example.StudentManagementAPI.config;

/**
 * Holds the "acting user" (email + role) for the current request thread.
 * Populated by AuditHeaderFilter from the X-User-Email / X-User-Role
 * headers the frontend sends, and always cleared at the end of the
 * request so it never leaks onto a pooled thread for a different request.
 */
public class CurrentUserContext {

    private static final ThreadLocal<String> CURRENT_USER = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_ROLE = new ThreadLocal<>();

    private CurrentUserContext() {
    }

    public static void set(String email) {
        CURRENT_USER.set(email);
    }

    public static String get() {
        return CURRENT_USER.get();
    }

    public static void setRole(String role) {
        CURRENT_ROLE.set(role);
    }

    public static String getRole() {
        return CURRENT_ROLE.get();
    }

    public static void clear() {
        CURRENT_USER.remove();
        CURRENT_ROLE.remove();
    }
}