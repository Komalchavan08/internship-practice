package com.internship.contractmanagement.controller;

import com.internship.contractmanagement.dto.AuditLogRequest;
import com.internship.contractmanagement.dto.AuditLogResponse;
import com.internship.contractmanagement.service.AuditLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public ResponseEntity<AuditLogResponse> createLog(@Valid @RequestBody AuditLogRequest request) {
        return new ResponseEntity<>(auditLogService.createLog(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponse> getLogById(@PathVariable Long id) {
        return ResponseEntity.ok(auditLogService.getLogById(id));
    }

    // GET /api/audit-logs/entity/CONTRACT/5 -> full history for contract id=5
    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<AuditLogResponse>> getLogsForEntity(@PathVariable String entityType,
                                                                   @PathVariable Long entityId) {
        return ResponseEntity.ok(auditLogService.getLogsForEntity(entityType, entityId));
    }

    // Intentionally NO PUT, NO DELETE - see AuditLogService for why
}