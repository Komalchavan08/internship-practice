package com.internship.contractmanagement.service;

import com.internship.contractmanagement.dto.AuditLogRequest;
import com.internship.contractmanagement.dto.AuditLogResponse;
import com.internship.contractmanagement.entity.AuditLog;
import com.internship.contractmanagement.entity.User;
import com.internship.contractmanagement.exception.ResourceNotFoundException;
import com.internship.contractmanagement.repository.AuditLogRepository;
import com.internship.contractmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Deliberately has NO update() and NO delete() methods.
 * An audit log's entire purpose is to be a trustworthy, tamper-proof record
 * of what happened. If it could be edited or deleted, it couldn't be trusted
 * as evidence - so this service (and its Controller) only expose Create + Read.
 */
@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public AuditLogResponse createLog(AuditLogRequest request) {
        User performedBy = userRepository.findById(request.getPerformedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getPerformedByUserId()));

        AuditLog log = new AuditLog();
        log.setEntityType(request.getEntityType());
        log.setEntityId(request.getEntityId());
        log.setAction(request.getAction());
        log.setPerformedBy(performedBy);
        log.setDetails(request.getDetails());

        return mapToResponse(auditLogRepository.save(log));
    }

    public AuditLogResponse getLogById(Long id) {
        AuditLog log = auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log not found with id: " + id));
        return mapToResponse(log);
    }

    public List<AuditLogResponse> getAllLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Handy for "show me the full history of this one contract/version"
    public List<AuditLogResponse> getLogsForEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AuditLogResponse mapToResponse(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getEntityType(),
                log.getEntityId(),
                log.getAction(),
                log.getPerformedBy().getFullName(),
                log.getTimestamp(),
                log.getDetails()
        );
    }
}