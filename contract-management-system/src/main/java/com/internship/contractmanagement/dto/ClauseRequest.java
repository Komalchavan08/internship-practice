package com.internship.contractmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClauseRequest {

    @NotNull(message = "versionId is required")
    private Long versionId;

    @NotBlank(message = "clauseTitle is required")
    private String clauseTitle;

    @NotBlank(message = "clauseText is required")
    private String clauseText;

    @NotNull(message = "clauseOrder is required")
    private Integer clauseOrder;
}