package com.internship.contractmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponse {

    private Long id;
    private String title;
    private String description;
    private String status;          // enum shown as plain text, e.g. "DRAFT"
    private String createdByName;   // flattened from the User relationship - client doesn't need the whole User object
    private LocalDateTime createdAt;
    private Long currentVersionId;  // just the ID, not the whole nested Version object
}
