package com.internship.contractmanagement.repository;

import com.internship.contractmanagement.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findByVersionId(Long versionId);
    List<Approval> findByApproverId(Long approverId); // e.g. "show this approver their pending reviews"
}