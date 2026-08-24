package com.internship.contractmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The actual uploaded file (PDF/DOCX) tied to one Version.
 * We store the file on disk (or cloud storage later) and keep
 * only the path/metadata here in the database.
 */
@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One version has exactly one document file
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false, unique = true)
    private Version version;

    @Column(nullable = false, length = 255)
    private String fileName;

    // Where the file physically lives on disk/cloud storage
    @Column(nullable = false, length = 500)
    private String filePath;

    // e.g. "application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    @Column(length = 100)
    private String fileType;

    // Size in bytes - useful for validation and display
    private Long fileSize;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
}
