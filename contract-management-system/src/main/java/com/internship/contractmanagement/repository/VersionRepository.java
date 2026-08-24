package com.internship.contractmanagement.repository;

import com.internship.contractmanagement.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionRepository extends JpaRepository<Version, Long> {

    // Auto-generates: SELECT * FROM versions WHERE contract_id = ?
    // Used to list every version that belongs to a specific contract.
    List<Version> findByContractId(Long contractId);

    // Used by the service to work out the NEXT version number for a contract
    // (count existing versions, then +1)
    int countByContractId(Long contractId);
}
