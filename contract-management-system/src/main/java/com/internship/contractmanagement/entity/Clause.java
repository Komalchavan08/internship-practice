package com.internship.contractmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A single clause within a contract Version, e.g. "Clause 4: Payment Terms".
 * Storing clauses individually (instead of one giant text blob) is what
 * lets us later show "exactly which clause changed" between two versions.
 */
@Entity
@Table(name = "clauses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clause {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which version this clause belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;

    @Column(nullable = false, length = 255)
    private String clauseTitle;

    // NOTE: deliberately NOT using @Lob here - combined with PostgreSQL,
    // @Lob makes Hibernate treat this as a live stream that can fail with
    // "Unable to access lob stream" once the transaction closes.
    // columnDefinition = "TEXT" alone is enough for long text in Postgres.
    @Column(nullable = false, columnDefinition = "TEXT")
    private String clauseText;

    // Order in which the clause appears in the document (1, 2, 3...)
    @Column(nullable = false)
    private Integer clauseOrder;
}