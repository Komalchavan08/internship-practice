package com.internship.contractmanagement.service;

import com.internship.contractmanagement.dto.VersionRequest;
import com.internship.contractmanagement.dto.VersionResponse;
import com.internship.contractmanagement.entity.Contract;
import com.internship.contractmanagement.entity.User;
import com.internship.contractmanagement.entity.Version;
import com.internship.contractmanagement.entity.VersionStatus;
import com.internship.contractmanagement.exception.ResourceNotFoundException;
import com.internship.contractmanagement.repository.ContractRepository;
import com.internship.contractmanagement.repository.UserRepository;
import com.internship.contractmanagement.repository.VersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VersionService {

    private final VersionRepository versionRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    public VersionService(VersionRepository versionRepository,
                           ContractRepository contractRepository,
                           UserRepository userRepository) {
        this.versionRepository = versionRepository;
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    // ---------- CREATE ----------
    public VersionResponse createVersion(VersionRequest request) {
        Contract contract = contractRepository.findById(request.getContractId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contract not found with id: " + request.getContractId()));

        User creator = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getCreatedByUserId()));

        // Business logic: work out the next version number ourselves.
        // If this contract already has 2 versions, the new one becomes v3.
        int existingCount = versionRepository.countByContractId(contract.getId());
        int nextVersionNumber = existingCount + 1;

        Version version = new Version();
        version.setContract(contract);
        version.setCreatedBy(creator);
        version.setVersionNumber(nextVersionNumber);
        version.setChangeSummary(request.getChangeSummary());
        version.setStatus(VersionStatus.DRAFT); // every new version starts as DRAFT

        Version saved = versionRepository.save(version);
        return mapToResponse(saved);
    }

    // ---------- READ (one) ----------
    public VersionResponse getVersionById(Long id) {
        Version version = versionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Version not found with id: " + id));
        return mapToResponse(version);
    }

    // ---------- READ (all versions for one contract) ----------
    public List<VersionResponse> getVersionsByContract(Long contractId) {
        return versionRepository.findByContractId(contractId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ---------- READ (all) ----------
    public List<VersionResponse> getAllVersions() {
        return versionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ---------- UPDATE ----------
    // Only the change summary is editable - version number and contract
    // link should never change once created (that would break history).
    public VersionResponse updateVersion(Long id, VersionRequest request) {
        Version version = versionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Version not found with id: " + id));

        version.setChangeSummary(request.getChangeSummary());

        Version updated = versionRepository.save(version);
        return mapToResponse(updated);
    }

    // ---------- DELETE ----------
    public void deleteVersion(Long id) {
        if (!versionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Version not found with id: " + id);
        }
        versionRepository.deleteById(id);
    }

    // ---------- Helper: Entity -> Response DTO ----------
    private VersionResponse mapToResponse(Version version) {
        return new VersionResponse(
                version.getId(),
                version.getContract().getId(),
                version.getVersionNumber(),
                version.getStatus().name(),
                version.getCreatedBy().getFullName(),
                version.getCreatedAt(),
                version.getChangeSummary()
        );
    }
}
