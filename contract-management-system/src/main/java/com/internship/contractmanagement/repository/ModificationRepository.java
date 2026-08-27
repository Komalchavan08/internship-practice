package com.internship.contractmanagement.repository;

import com.internship.contractmanagement.entity.Modification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModificationRepository extends JpaRepository<Modification, Long> {
    List<Modification> findByVersionId(Long versionId);
}