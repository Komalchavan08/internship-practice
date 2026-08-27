package com.internship.contractmanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApprovalRequest {

    @NotNull(message = "versionId is required")
    private Long versionId;

    @NotNull(message = "requestedByUserId is required")
    private Long requestedByUserId;

    @NotNull(message = "approverId is required")
    private Long approverId;

    private String comments;

    // NOTE: no "status" field here - every new Approval starts as PENDING.
    // Actually approving/rejecting is a dedicated workflow action (a future
    // task), not a plain field update, so it's not exposed on create.
}