package com.internship.contractmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClauseResponse {
    private Long id;
    private Long versionId;
    private String clauseTitle;
    private String clauseText;
    private Integer clauseOrder;
}