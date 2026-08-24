package com.internship.contractmanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VersionRequest {

    @NotNull(message = "contractId is required")
    private Long contractId;

    @NotNull(message = "createdByUserId is required")
    private Long createdByUserId;

    // Optional note describing the change, e.g. "Updated payment terms"
    private String changeSummary;

    // Deliberately NO versionNumber field here - the service calculates
    // it automatically (existing version count + 1). This stops a client
    // from ever sending a wrong/duplicate version number.
}
