package com.internship.contractmanagement.service;

import com.internship.contractmanagement.dto.ContractRequest;
import com.internship.contractmanagement.dto.ContractResponse;
import com.internship.contractmanagement.entity.Contract;
import com.internship.contractmanagement.entity.ContractStatus;
import com.internship.contractmanagement.entity.User;
import com.internship.contractmanagement.exception.ResourceNotFoundException;
import com.internship.contractmanagement.repository.ContractRepository;
import com.internship.contractmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final UserRepository userRepository; // needed to resolve createdByUserId -> User

    public ContractService(ContractRepository contractRepository, UserRepository userRepository) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    // ---------- CREATE ----------
    public ContractResponse createContract(ContractRequest request) {
        User creator = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getCreatedByUserId()));

        Contract contract = new Contract();
        contract.setTitle(request.getTitle());
        contract.setDescription(request.getDescription());
        contract.setCreatedBy(creator);
        contract.setStatus(ContractStatus.DRAFT); // every new contract starts as DRAFT

        Contract saved = contractRepository.save(contract);
        return mapToResponse(saved);
    }

    // ---------- READ (one) ----------
    public ContractResponse getContractById(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));
        return mapToResponse(contract);
    }

    // ---------- READ (all) ----------
    public List<ContractResponse> getAllContracts() {
        return contractRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ---------- UPDATE ----------
    // Deliberately only allows editing title/description here.
    // Status changes go through the future Approval workflow, not a raw update.
    public ContractResponse updateContract(Long id, ContractRequest request) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));

        contract.setTitle(request.getTitle());
        contract.setDescription(request.getDescription());

        Contract updated = contractRepository.save(contract);
        return mapToResponse(updated);
    }

    // ---------- DELETE ----------
    public void deleteContract(Long id) {
        if (!contractRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contract not found with id: " + id);
        }
        contractRepository.deleteById(id);
    }

    // ---------- Helper: Entity -> Response DTO ----------
    private ContractResponse mapToResponse(Contract contract) {
        return new ContractResponse(
                contract.getId(),
                contract.getTitle(),
                contract.getDescription(),
                contract.getStatus().name(), // enum -> plain string, e.g. ContractStatus.DRAFT -> "DRAFT"
                contract.getCreatedBy().getFullName(),
                contract.getCreatedAt(),
                contract.getCurrentVersion() != null ? contract.getCurrentVersion().getId() : null
        );
    }
}
