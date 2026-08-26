package com.internship.contractmanagement.repository;

import com.internship.contractmanagement.entity.Clause;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClauseRepository extends JpaRepository<Clause, Long> {
    List<Clause> findByVersionIdOrderByClauseOrderAsc(Long versionId);
}