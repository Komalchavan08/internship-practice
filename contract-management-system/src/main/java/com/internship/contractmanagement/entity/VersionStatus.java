package com.internship.contractmanagement.entity;

/**
 * Lifecycle status of a single ContractVersion (a snapshot/edit of a contract).
 * Maps directly to the workflow: Modify -> Save as DRAFT -> Submit
 * (PENDING_APPROVAL) -> Approve/Reject.
 */
public enum VersionStatus {
    DRAFT,
    PENDING_APPROVAL,
    APPROVED,
    REJECTED
}
