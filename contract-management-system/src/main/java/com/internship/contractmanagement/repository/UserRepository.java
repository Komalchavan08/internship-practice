package com.internship.contractmanagement.repository;

import com.internship.contractmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * By simply EXTENDING JpaRepository<User, Long>, we get full CRUD for free:
 * save(), findById(), findAll(), deleteById(), count()... with ZERO SQL written.
 * Spring Data JPA generates the implementation automatically at runtime.
 *
 * <User, Long> means: this repository manages User entities, whose ID type is Long.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA reads this METHOD NAME and auto-generates the SQL for it.
    // "findByEmail" -> SELECT * FROM users WHERE email = ?
    // No method body needed - just declaring the signature is enough.
    Optional<User> findByEmail(String email);
}
