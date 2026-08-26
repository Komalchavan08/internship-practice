package com.internship.contractmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Records exactly what changed for a single clause when a new Version
 * was created. This is what lets us answer "what exactly changed in
 * v2 vs v1" at a clause level, not just "something changed".
 */
@Entity
@Table(name = "modifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Modification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The (new) version this modification belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;

    // Which clause was changed (nullable in case the whole document changed, not one clause)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clause_id")
    private Clause clause;

    // See Clause.java for why @Lob is deliberately avoided here with PostgreSQL
    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    // Who made this specific change
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by", nullable = false)
    private User modifiedBy;

    @Column(nullable = false)
    private LocalDateTime modifiedAt = LocalDateTime.now();

    // Short human-readable description, e.g. "Changed payment due date from 30 to 45 days"
    @Column(length = 500)
    private String description;
}