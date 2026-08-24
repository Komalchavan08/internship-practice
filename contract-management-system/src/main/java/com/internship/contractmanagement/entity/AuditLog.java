package com.internship.contractmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A generic activity log entry, deliberately NOT tied to one specific
 * entity type. Instead it stores (entityType, entityId) as a pair -
 * e.g. ("CONTRACT", 5) or ("APPROVAL", 12) - so this single table can
 * log actions across every entity in the system.
 */
@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which type of entity this log entry is about, e.g. "CONTRACT", "VERSION", "APPROVAL"
    @Column(nullable = false, length = 50)
    private String entityType;

    // The id of that specific entity row
    @Column(nullable = false)
    private Long entityId;

    // What happened, e.g. "UPLOAD", "MODIFY", "SUBMIT_FOR_APPROVAL", "APPROVE", "REJECT"
    @Column(nullable = false, length = 50)
    private String action;

    // Who performed the action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    private User performedBy;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    // Any extra context, e.g. "Changed status from DRAFT to PENDING_APPROVAL"
    @Column(length = 1000)
    private String details;
}
