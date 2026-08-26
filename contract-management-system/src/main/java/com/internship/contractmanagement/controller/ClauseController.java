package com.internship.contractmanagement.controller;

import com.internship.contractmanagement.dto.ClauseRequest;
import com.internship.contractmanagement.dto.ClauseResponse;
import com.internship.contractmanagement.service.ClauseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clauses")
public class ClauseController {

    private final ClauseService clauseService;

    public ClauseController(ClauseService clauseService) {
        this.clauseService = clauseService;
    }

    @PostMapping
    public ResponseEntity<ClauseResponse> createClause(@Valid @RequestBody ClauseRequest request) {
        return new ResponseEntity<>(clauseService.createClause(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClauseResponse>> getAllClauses() {
        return ResponseEntity.ok(clauseService.getAllClauses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClauseResponse> getClauseById(@PathVariable Long id) {
        return ResponseEntity.ok(clauseService.getClauseById(id));
    }

    @GetMapping("/version/{versionId}")
    public ResponseEntity<List<ClauseResponse>> getClausesByVersion(@PathVariable Long versionId) {
        return ResponseEntity.ok(clauseService.getClausesByVersion(versionId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClauseResponse> updateClause(@PathVariable Long id, @Valid @RequestBody ClauseRequest request) {
        return ResponseEntity.ok(clauseService.updateClause(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClause(@PathVariable Long id) {
        clauseService.deleteClause(id);
        return ResponseEntity.noContent().build();
    }
}