package com.internship.contractmanagement.entity;

/**
 * Overall lifecycle status of a Contract (the top-level container).
 * This is separate from VersionStatus - a Contract stays ACTIVE
 * while its individual Versions go through their own DRAFT -> APPROVED cycle.
 */
public enum ContractStatus {
    DRAFT,
    ACTIVE,
    ARCHIVED
}
