package com.internship.contractmanagement.service;

import com.internship.contractmanagement.dto.ClauseRequest;
import com.internship.contractmanagement.dto.ClauseResponse;
import com.internship.contractmanagement.entity.Clause;
import com.internship.contractmanagement.entity.Version;
import com.internship.contractmanagement.exception.ResourceNotFoundException;
import com.internship.contractmanagement.repository.ClauseRepository;
import com.internship.contractmanagement.repository.VersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClauseService {

    private final ClauseRepository clauseRepository;
    private final VersionRepository versionRepository;

    public ClauseService(ClauseRepository clauseRepository, VersionRepository versionRepository) {
        this.clauseRepository = clauseRepository;
        this.versionRepository = versionRepository;
    }

    public ClauseResponse createClause(ClauseRequest request) {
        Version version = versionRepository.findById(request.getVersionId())
                .orElseThrow(() -> new ResourceNotFoundException("Version not found with id: " + request.getVersionId()));

        Clause clause = new Clause();
        clause.setVersion(version);
        clause.setClauseTitle(request.getClauseTitle());
        clause.setClauseText(request.getClauseText());
        clause.setClauseOrder(request.getClauseOrder());

        return mapToResponse(clauseRepository.save(clause));
    }

    public ClauseResponse getClauseById(Long id) {
        Clause clause = clauseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clause not found with id: " + id));
        return mapToResponse(clause);
    }

    public List<ClauseResponse> getClausesByVersion(Long versionId) {
        return clauseRepository.findByVersionIdOrderByClauseOrderAsc(versionId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ClauseResponse> getAllClauses() {
        return clauseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ClauseResponse updateClause(Long id, ClauseRequest request) {
        Clause clause = clauseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clause not found with id: " + id));

        clause.setClauseTitle(request.getClauseTitle());
        clause.setClauseText(request.getClauseText());
        clause.setClauseOrder(request.getClauseOrder());

        return mapToResponse(clauseRepository.save(clause));
    }

    public void deleteClause(Long id) {
        if (!clauseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Clause not found with id: " + id);
        }
        clauseRepository.deleteById(id);
    }

    private ClauseResponse mapToResponse(Clause clause) {
        return new ClauseResponse(
                clause.getId(),
                clause.getVersion().getId(),
                clause.getClauseTitle(),
                clause.getClauseText(),
                clause.getClauseOrder()
        );
    }
}