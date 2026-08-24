package com.internship.contractmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionResponse {

    private Long id;
    private Long contractId;
    private Integer versionNumber;
    private String status;
    private String createdByName;
    private LocalDateTime createdAt;
    private String changeSummary;
}
