package com.example.StudentManagementAPI.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // LOGIN, LOGOUT, REGISTRATION, CREATE, UPDATE, DELETE, PASSWORD_CHANGE
    private String action;

    // Email of whoever performed the action, or "SYSTEM" if unknown
    private String performedBy;

    // Which kind of record was affected, e.g. "Student", "User" — null for
    // events that aren't tied to a specific record (Login/Logout)
    private String entityType;

    // The affected record's id, as a string — null when not applicable
    private String entityId;

    private String description;

    private LocalDateTime timestamp;

    public AuditLog() {
    }

    public AuditLog(String action, String performedBy, String entityType, String entityId, String description) {
        this.action = action;
        this.performedBy = performedBy;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}