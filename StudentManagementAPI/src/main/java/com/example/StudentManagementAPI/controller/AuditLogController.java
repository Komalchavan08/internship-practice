package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.config.CurrentUserContext;
import com.example.StudentManagementAPI.entity.AuditLog;
import com.example.StudentManagementAPI.exception.AccessDeniedException;
import com.example.StudentManagementAPI.service.AuditLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogService service;

    // Only Admin can view or export logs. Enforced here via the X-User-Role
    // header (same interim pattern as the rest of the app) until JWT +
    // Protected Routes replace it with real server-side session checking.
    private void requireAdmin() {
        String role = CurrentUserContext.getRole();
        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            throw new AccessDeniedException("Only an admin can view activity logs.");
        }
    }

    @GetMapping
    public ResponseEntity<Page<AuditLog>> getLogs(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        requireAdmin();

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));

        return ResponseEntity.ok(service.getLogs(action, performedBy, entityType, from, to, pageable));
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportCsv(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String performedBy,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        requireAdmin();

        List<AuditLog> logs = service.getLogsForExport(action, performedBy, entityType, from, to);

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Action,Performed By,Entity Type,Entity ID,Description,Timestamp\n");

        for (AuditLog log : logs) {
            csv.append(log.getId()).append(",")
                    .append(csvSafe(log.getAction())).append(",")
                    .append(csvSafe(log.getPerformedBy())).append(",")
                    .append(csvSafe(log.getEntityType())).append(",")
                    .append(csvSafe(log.getEntityId())).append(",")
                    .append(csvSafe(log.getDescription())).append(",")
                    .append(log.getTimestamp()).append("\n");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "activity-logs.csv");

        return ResponseEntity.ok().headers(headers).body(csv.toString());
    }

    // Wraps a field in quotes and escapes any embedded quotes, so commas or
    // quotes inside a description don't corrupt the CSV structure.
    private String csvSafe(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}