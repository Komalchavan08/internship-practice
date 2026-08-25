package com.internship.contractmanagement.repository;

import com.internship.contractmanagement.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Used to check "does this version already have a document?"
    Optional<Document> findByVersionId(Long versionId);
}