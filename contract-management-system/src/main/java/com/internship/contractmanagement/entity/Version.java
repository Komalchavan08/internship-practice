package com.internship.contractmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A single snapshot of a Contract at a point in time (v1, v2, v3...).
 * This is the heart of version control: every modification creates a
 * NEW Version row instead of overwriting an old one. Old versions are
 * never deleted, so the full history is always preserved.
 */
@Entity
@Table(name = "versions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which contract this version belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    // 1, 2, 3... increases with every edit to this contract
    @Column(nullable = false)
    private Integer versionNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VersionStatus status = VersionStatus.DRAFT;

    // Who created this particular version (i.e. who made this edit)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Optional note describing what changed in this version,
    // e.g. "Updated payment terms clause"
    @Column(length = 500)
    private String changeSummary;
}
