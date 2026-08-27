package com.internship.contractmanagement.service;

import com.internship.contractmanagement.dto.ApprovalRequest;
import com.internship.contractmanagement.dto.ApprovalResponse;
import com.internship.contractmanagement.entity.Approval;
import com.internship.contractmanagement.entity.ApprovalStatus;
import com.internship.contractmanagement.entity.User;
import com.internship.contractmanagement.entity.Version;
import com.internship.contractmanagement.exception.ResourceNotFoundException;
import com.internship.contractmanagement.repository.ApprovalRepository;
import com.internship.contractmanagement.repository.UserRepository;
import com.internship.contractmanagement.repository.VersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final VersionRepository versionRepository;
    private final UserRepository userRepository;

    public ApprovalService(ApprovalRepository approvalRepository,
                           VersionRepository versionRepository,
                           UserRepository userRepository) {
        this.approvalRepository = approvalRepository;
        this.versionRepository = versionRepository;
        this.userRepository = userRepository;
    }

    public ApprovalResponse createApproval(ApprovalRequest request) {
        Version version = versionRepository.findById(request.getVersionId())
                .orElseThrow(() -> new ResourceNotFoundException("Version not found with id: " + request.getVersionId()));

        // Two DIFFERENT User lookups from two DIFFERENT ids on the same request -
        // this is the new thing here compared to earlier services
        User requestedBy = userRepository.findById(request.getRequestedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getRequestedByUserId()));

        User approver = userRepository.findById(request.getApproverId())
                .orElseThrow(() -> new ResourceNotFoundException("Approver not found with id: " + request.getApproverId()));

        Approval approval = new Approval();
        approval.setVersion(version);
        approval.setRequestedBy(requestedBy);
        approval.setApprover(approver);
        approval.setComments(request.getComments());
        approval.setStatus(ApprovalStatus.PENDING); // always starts PENDING

        return mapToResponse(approvalRepository.save(approval));
    }

    public ApprovalResponse getApprovalById(Long id) {
        Approval approval = approvalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Approval not found with id: " + id));
        return mapToResponse(approval);
    }

    public List<ApprovalResponse> getApprovalsByVersion(Long versionId) {
        return approvalRepository.findByVersionId(versionId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ApprovalResponse> getAllApprovals() {
        return approvalRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Only comments are editable here. Changing STATUS (approve/reject) is
    // a dedicated workflow action coming in a future task, not a plain update.
    public ApprovalResponse updateApproval(Long id, ApprovalRequest request) {
        Approval approval = approvalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Approval not found with id: " + id));

        approval.setComments(request.getComments());

        return mapToResponse(approvalRepository.save(approval));
    }

    public void deleteApproval(Long id) {
        if (!approvalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Approval not found with id: " + id);
        }
        approvalRepository.deleteById(id);
    }

    private ApprovalResponse mapToResponse(Approval approval) {
        return new ApprovalResponse(
                approval.getId(),
                approval.getVersion().getId(),
                approval.getRequestedBy().getFullName(),
                approval.getApprover().getFullName(),
                approval.getStatus().name(),
                approval.getComments(),
                approval.getRequestedAt(),
                approval.getDecidedAt()
        );
    }
}