package com.internship.contractmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditLogRequest {

    @NotBlank(message = "entityType is required")
    private String entityType; // e.g. "CONTRACT", "VERSION", "APPROVAL"

    @NotNull(message = "entityId is required")
    private Long entityId;

    @NotBlank(message = "action is required")
    private String action; // e.g. "UPLOAD", "MODIFY", "APPROVE", "REJECT"

    @NotNull(message = "performedByUserId is required")
    private Long performedByUserId;

    private String details;
}