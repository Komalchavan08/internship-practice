package com.internship.contractmanagement.controller;

import com.internship.contractmanagement.dto.ModificationRequest;
import com.internship.contractmanagement.dto.ModificationResponse;
import com.internship.contractmanagement.service.ModificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modifications")
public class ModificationController {

    private final ModificationService modificationService;

    public ModificationController(ModificationService modificationService) {
        this.modificationService = modificationService;
    }

    @PostMapping
    public ResponseEntity<ModificationResponse> createModification(@Valid @RequestBody ModificationRequest request) {
        return new ResponseEntity<>(modificationService.createModification(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ModificationResponse>> getAllModifications() {
        return ResponseEntity.ok(modificationService.getAllModifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModificationResponse> getModificationById(@PathVariable Long id) {
        return ResponseEntity.ok(modificationService.getModificationById(id));
    }

    @GetMapping("/version/{versionId}")
    public ResponseEntity<List<ModificationResponse>> getModificationsByVersion(@PathVariable Long versionId) {
        return ResponseEntity.ok(modificationService.getModificationsByVersion(versionId));
    }

    // Intentionally NO PUT endpoint - modifications are historical records
    // and shouldn't be edited after creation (see ModificationService comment)

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModification(@PathVariable Long id) {
        modificationService.deleteModification(id);
        return ResponseEntity.noContent().build();
    }
}