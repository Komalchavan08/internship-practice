package com.internship.contractmanagement.controller;

import com.internship.contractmanagement.dto.VersionRequest;
import com.internship.contractmanagement.dto.VersionResponse;
import com.internship.contractmanagement.service.VersionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/versions")
public class VersionController {

    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @PostMapping
    public ResponseEntity<VersionResponse> createVersion(@Valid @RequestBody VersionRequest request) {
        return new ResponseEntity<>(versionService.createVersion(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VersionResponse>> getAllVersions() {
        return ResponseEntity.ok(versionService.getAllVersions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VersionResponse> getVersionById(@PathVariable Long id) {
        return ResponseEntity.ok(versionService.getVersionById(id));
    }

    // GET /api/versions/contract/5 -> all versions belonging to contract id=5
    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<VersionResponse>> getVersionsByContract(@PathVariable Long contractId) {
        return ResponseEntity.ok(versionService.getVersionsByContract(contractId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VersionResponse> updateVersion(@PathVariable Long id,
                                                         @Valid @RequestBody VersionRequest request) {
        return ResponseEntity.ok(versionService.updateVersion(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVersion(@PathVariable Long id) {
        versionService.deleteVersion(id);
        return ResponseEntity.noContent().build();
    }
}
