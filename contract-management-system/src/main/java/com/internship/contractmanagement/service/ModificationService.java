package com.internship.contractmanagement.service;

import com.internship.contractmanagement.dto.ModificationRequest;
import com.internship.contractmanagement.dto.ModificationResponse;
import com.internship.contractmanagement.entity.Clause;
import com.internship.contractmanagement.entity.Modification;
import com.internship.contractmanagement.entity.User;
import com.internship.contractmanagement.entity.Version;
import com.internship.contractmanagement.exception.ResourceNotFoundException;
import com.internship.contractmanagement.repository.ClauseRepository;
import com.internship.contractmanagement.repository.ModificationRepository;
import com.internship.contractmanagement.repository.UserRepository;
import com.internship.contractmanagement.repository.VersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModificationService {

    private final ModificationRepository modificationRepository;
    private final VersionRepository versionRepository;
    private final ClauseRepository clauseRepository;
    private final UserRepository userRepository;

    public ModificationService(ModificationRepository modificationRepository,
                               VersionRepository versionRepository,
                               ClauseRepository clauseRepository,
                               UserRepository userRepository) {
        this.modificationRepository = modificationRepository;
        this.versionRepository = versionRepository;
        this.clauseRepository = clauseRepository;
        this.userRepository = userRepository;
    }

    public ModificationResponse createModification(ModificationRequest request) {
        Version version = versionRepository.findById(request.getVersionId())
                .orElseThrow(() -> new ResourceNotFoundException("Version not found with id: " + request.getVersionId()));

        User modifiedBy = userRepository.findById(request.getModifiedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getModifiedByUserId()));

        // clauseId is OPTIONAL - only look it up if the client actually sent one
        Clause clause = null;
        if (request.getClauseId() != null) {
            clause = clauseRepository.findById(request.getClauseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clause not found with id: " + request.getClauseId()));
        }

        Modification modification = new Modification();
        modification.setVersion(version);
        modification.setClause(clause); // may be null - that's allowed, see entity
        modification.setOldValue(request.getOldValue());
        modification.setNewValue(request.getNewValue());
        modification.setModifiedBy(modifiedBy);
        modification.setDescription(request.getDescription());

        return mapToResponse(modificationRepository.save(modification));
    }

    public ModificationResponse getModificationById(Long id) {
        Modification modification = modificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Modification not found with id: " + id));
        return mapToResponse(modification);
    }

    public List<ModificationResponse> getModificationsByVersion(Long versionId) {
        return modificationRepository.findByVersionId(versionId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ModificationResponse> getAllModifications() {
        return modificationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteModification(Long id) {
        if (!modificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Modification not found with id: " + id);
        }
        modificationRepository.deleteById(id);
    }

    private ModificationResponse mapToResponse(Modification modification) {
        return new ModificationResponse(
                modification.getId(),
                modification.getVersion().getId(),
                modification.getClause() != null ? modification.getClause().getId() : null,
                modification.getOldValue(),
                modification.getNewValue(),
                modification.getModifiedBy().getFullName(),
                modification.getModifiedAt(),
                modification.getDescription()
        );
    }
}