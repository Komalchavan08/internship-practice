package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.config.CurrentUserContext;
import com.example.StudentManagementAPI.entity.AuditLog;
import com.example.StudentManagementAPI.repository.AuditLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository repository;

    /**
     * Records one audit log entry. Called from every other service on
     * Login/Logout/Registration/Create/Update/Delete/Password Change —
     * "performedBy" always comes from CurrentUserContext (the X-User-Email
     * header), so callers never have to pass it explicitly.
     */
    public void log(String action, String entityType, String entityId, String description) {

        String performedBy = CurrentUserContext.get();

        AuditLog entry = new AuditLog(
                action,
                (performedBy == null || performedBy.isBlank()) ? "SYSTEM" : performedBy,
                entityType,
                entityId,
                description
        );

        repository.save(entry);
    }

    public Page<AuditLog> getLogs(String action, String performedBy, String entityType,
                                  LocalDateTime from, LocalDateTime to, Pageable pageable) {

        return repository.findAll(buildFilter(action, performedBy, entityType, from, to), pageable);
    }

    public List<AuditLog> getLogsForExport(String action, String performedBy, String entityType,
                                           LocalDateTime from, LocalDateTime to) {

        return repository.findAll(buildFilter(action, performedBy, entityType, from, to));
    }

    private Specification<AuditLog> buildFilter(String action, String performedBy, String entityType,
                                                LocalDateTime from, LocalDateTime to) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (action != null && !action.isBlank()) {
                predicates.add(cb.equal(root.get("action"), action.trim().toUpperCase()));
            }

            if (performedBy != null && !performedBy.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("performedBy")), "%" + performedBy.trim().toLowerCase() + "%"));
            }

            if (entityType != null && !entityType.isBlank()) {
                predicates.add(cb.equal(root.get("entityType"), entityType.trim()));
            }

            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), from));
            }

            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}