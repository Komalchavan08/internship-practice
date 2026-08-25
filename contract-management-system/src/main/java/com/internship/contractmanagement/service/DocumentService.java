package com.internship.contractmanagement.service;

import com.internship.contractmanagement.dto.DocumentRequest;
import com.internship.contractmanagement.dto.DocumentResponse;
import com.internship.contractmanagement.entity.Document;
import com.internship.contractmanagement.entity.Version;
import com.internship.contractmanagement.exception.ResourceNotFoundException;
import com.internship.contractmanagement.repository.DocumentRepository;
import com.internship.contractmanagement.repository.VersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final VersionRepository versionRepository;

    public DocumentService(DocumentRepository documentRepository, VersionRepository versionRepository) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
    }

    public DocumentResponse createDocument(DocumentRequest request) {
        Version version = versionRepository.findById(request.getVersionId())
                .orElseThrow(() -> new ResourceNotFoundException("Version not found with id: " + request.getVersionId()));

        // Enforce the ONE-TO-ONE rule ourselves: the @JoinColumn(unique = true)
        // on Document.java would also catch this at the DATABASE level with a
        // hard-to-read SQL error, but checking here first lets us return a
        // clean, readable error message instead.
        if (documentRepository.findByVersionId(version.getId()).isPresent()) {
            throw new IllegalStateException("This version already has a document attached. Delete it first or update the existing one.");
        }

        Document document = new Document();
        document.setVersion(version);
        document.setFileName(request.getFileName());
        document.setFilePath(request.getFilePath());
        document.setFileType(request.getFileType());
        document.setFileSize(request.getFileSize());

        Document saved = documentRepository.save(document);
        return mapToResponse(saved);
    }

    public DocumentResponse getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
        return mapToResponse(document);
    }

    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DocumentResponse updateDocument(Long id, DocumentRequest request) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        document.setFileName(request.getFileName());
        document.setFilePath(request.getFilePath());
        document.setFileType(request.getFileType());
        document.setFileSize(request.getFileSize());

        return mapToResponse(documentRepository.save(document));
    }

    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Document not found with id: " + id);
        }
        documentRepository.deleteById(id);
    }

    private DocumentResponse mapToResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getVersion().getId(),
                document.getFileName(),
                document.getFilePath(),
                document.getFileType(),
                document.getFileSize(),
                document.getUploadedAt()
        );
    }
}