package com.internship.contractmanagement.repository;

import com.internship.contractmanagement.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
