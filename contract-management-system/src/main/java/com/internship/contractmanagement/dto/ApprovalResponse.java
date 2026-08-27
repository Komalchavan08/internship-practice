package com.internship.contractmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalResponse {
    private Long id;
    private Long versionId;
    private String requestedByName;
    private String approverName;
    private String status;
    private String comments;
    private LocalDateTime requestedAt;
    private LocalDateTime decidedAt; // null until a decision is made
}