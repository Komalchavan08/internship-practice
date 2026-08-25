package com.internship.contractmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentRequest {

    @NotNull(message = "versionId is required")
    private Long versionId;

    @NotBlank(message = "fileName is required")
    private String fileName;

    @NotBlank(message = "filePath is required")
    private String filePath;

    private String fileType;
    private Long fileSize;

    // NOTE: this only stores file METADATA (name/path/size). Actual file
    // upload (multipart handling + saving bytes to disk) is a separate task.
}