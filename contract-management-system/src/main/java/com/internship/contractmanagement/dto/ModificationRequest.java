package com.internship.contractmanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModificationRequest {

    @NotNull(message = "versionId is required")
    private Long versionId;

    // Nullable on purpose: a modification might describe a whole-document
    // change rather than one specific clause
    private Long clauseId;

    private String oldValue;
    private String newValue;

    @NotNull(message = "modifiedByUserId is required")
    private Long modifiedByUserId;

    private String description;
}