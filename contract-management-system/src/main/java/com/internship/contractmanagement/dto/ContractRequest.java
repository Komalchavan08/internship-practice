package com.internship.contractmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContractRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    // Which user is creating this contract - client sends the user's ID,
    // service layer looks up the actual User entity from it
    @NotNull(message = "createdByUserId is required")
    private Long createdByUserId;
}
