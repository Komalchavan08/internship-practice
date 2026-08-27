package com.internship.contractmanagement.controller;

import com.internship.contractmanagement.dto.ApprovalRequest;
import com.internship.contractmanagement.dto.ApprovalResponse;
import com.internship.contractmanagement.service.ApprovalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping
    public ResponseEntity<ApprovalResponse> createApproval(@Valid @RequestBody ApprovalRequest request) {
        return new ResponseEntity<>(approvalService.createApproval(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ApprovalResponse>> getAllApprovals() {
        return ResponseEntity.ok(approvalService.getAllApprovals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprovalResponse> getApprovalById(@PathVariable Long id) {
        return ResponseEntity.ok(approvalService.getApprovalById(id));
    }

    @GetMapping("/version/{versionId}")
    public ResponseEntity<List<ApprovalResponse>> getApprovalsByVersion(@PathVariable Long versionId) {
        return ResponseEntity.ok(approvalService.getApprovalsByVersion(versionId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApprovalResponse> updateApproval(@PathVariable Long id, @Valid @RequestBody ApprovalRequest request) {
        return ResponseEntity.ok(approvalService.updateApproval(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApproval(@PathVariable Long id) {
        approvalService.deleteApproval(id);
        return ResponseEntity.noContent().build();
    }
}