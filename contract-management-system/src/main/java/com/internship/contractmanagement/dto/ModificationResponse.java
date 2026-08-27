package com.internship.contractmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModificationResponse {
    private Long id;
    private Long versionId;
    private Long clauseId; // may be null
    private String oldValue;
    private String newValue;
    private String modifiedByName;
    private LocalDateTime modifiedAt;
    private String description;
}