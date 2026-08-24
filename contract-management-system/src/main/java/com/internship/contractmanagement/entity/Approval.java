package com.internship.contractmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A single approval request tied to one Version. Created when a version
 * is submitted for review; updated when the approver makes a decision.
 * One approver per version (kept simple for this project's scope).
 */
@Entity
@Table(name = "approvals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which version is being reviewed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;

    // Who submitted this version for approval
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    // Who is responsible for approving/rejecting it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    // Approver's remarks, especially useful when rejecting
    @Column(length = 1000)
    private String comments;

    @Column(nullable = false)
    private LocalDateTime requestedAt = LocalDateTime.now();

    // Null until the approver actually makes a decision
    private LocalDateTime decidedAt;
}
